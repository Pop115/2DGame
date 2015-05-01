package BasePackage;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class Entity {
	Niveau niveau;
	double posX, posY;
	double vectorX, vectorY;
	int masse=20;
	int speed;
	Random rand = new Random();
	PanneauGame panGame;
	Color couleur = Color.blue;
	int marge =2;
	double rotation=0; //En radians
	double modZ;
	int d,x,y,r;
	BufferedImage image;
	Graphics2D g2;
	
	public Entity(Niveau niveau){
		try {
			image = ImageIO.read(new File("notDefined.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sayInfos(){
		System.out.println("--------------------------------------------------------------");
		System.out.println("posX:"+posX+" posY:"+posY);
		System.out.println("vectorX:"+vectorX+" vectorY:"+vectorY);
	}
	
	public void spawnRandom(){		//Place l'entité aléatoirement
		do{
			posX=(double)rand.nextInt((niveau.getArraySizeX()-2)*(int)panGame.cellSizeX);
			posY=(double)rand.nextInt((niveau.getArraySizeY()-2)*(int)panGame.cellSizeY);
		}while(niveau.getArray()[(int)(posX/panGame.cellSizeX)][(int)(posY/panGame.cellSizeY)]!=0);
		System.out.println("Spawned at x:"+posX+" y:"+posY);
	}
	
	public int[][] nuke(int[][] array, int rayon){
		try{
			for(int r=0;r<rayon;r++){
				//Algorithme de tracé de cercle d'Andres
				x=0;
				y=r;
				d=r-1;
				while(y>=x){
					array[(int)(posX/panGame.cellSizeX) + x][(int)(posY/panGame.cellSizeY) + y]=0;
					array[(int)(posX/panGame.cellSizeX) + y][(int)(posY/panGame.cellSizeY) + x]=0;
					array[(int)(posX/panGame.cellSizeX) - x][(int)(posY/panGame.cellSizeY) + y]=0;
					array[(int)(posX/panGame.cellSizeX) - y][(int)(posY/panGame.cellSizeY) + x]=0;
					array[(int)(posX/panGame.cellSizeX) + x][(int)(posY/panGame.cellSizeY) - y]=0;
					array[(int)(posX/panGame.cellSizeX) + y][(int)(posY/panGame.cellSizeY) - x]=0;
					array[(int)(posX/panGame.cellSizeX) - x][(int)(posY/panGame.cellSizeY) - y]=0;
					array[(int)(posX/panGame.cellSizeX) - y][(int)(posY/panGame.cellSizeY) - x]=0;

					if(d>=2*x){
						d=d-2*x-1;
						x=x+1;
					}
					else if(d<2*(r-y)){
						d=d+2*y-1;
						y=y-1;
					}
					else{
						d=d+2*(y-x-1);
						y=y-1;
						x=x+1;
					}
				}
			}
		}catch(ArrayIndexOutOfBoundsException e){
			e.printStackTrace();
		}
		return array;
	}
	
	public void setImage(BufferedImage img){
		image=img;
	}
	
	public BufferedImage getImage(){
		return image;
	}
	
	public double getRotation(){
		return rotation;
	}
	
	public double getRotationWithVectors(){
		modZ=Math.sqrt(Math.pow(vectorX, 2)+Math.pow(vectorY, 2));
		rotation=Math.acos(vectorX/modZ);
		return rotation;
	}
	

  	public void collide(){
		try{
			//En bas et en haut à gauche, axe X
			if(niveau.getArray()[(int)(posX+vectorX)/(int)panGame.cellSizeX][(int)((posY-marge)/panGame.cellSizeY)+1]!=0 || niveau.getArray()[(int)(posX+vectorX)/(int)panGame.cellSizeX][(int)posY/(int)panGame.cellSizeY]!=0){
				vectorX=0;
				couleur=Color.red;
			}
			
			//En bas et en haut à droite, axe X
			if(niveau.getArray()[(int)((posX+vectorX-marge)/panGame.cellSizeX)+1][(int)((posY-marge)/panGame.cellSizeY)+1]!=0  || niveau.getArray()[(int)((posX+vectorX-marge)/panGame.cellSizeX)+1][(int)posY/(int)panGame.cellSizeY]!=0){
				vectorX=0;
				couleur=Color.red;
			}
			
			//A droite et à gauche, en haut, axe Y
			if(niveau.getArray()[(int)((posX-marge)/panGame.cellSizeX)+1][(int)((posY+vectorY)/panGame.cellSizeY)]!=0  || niveau.getArray()[(int)(posX)/(int)panGame.cellSizeX][(int)((posY+vectorY)/(int)panGame.cellSizeY)]!=0){
				vectorY=0;
				couleur=Color.red;
			}
			
			//A droite et à gauche, en bas, axe Y
			if(niveau.getArray()[(int)((posX-marge)/panGame.cellSizeX)+1][(int)((posY+vectorY-marge)/panGame.cellSizeY)+1]!=0 || niveau.getArray()[(int)(posX)/(int)panGame.cellSizeX][(int)((posY+vectorY-marge)/(int)panGame.cellSizeY)+1]!=0){
				vectorY=0;
				couleur=Color.red;
			}
			
		}catch(ArrayIndexOutOfBoundsException e){
			e.printStackTrace();
		}
	}
	
	
	public void setNiveau(Niveau niv){
		niveau = niv;
	}
	
	public void setPanneauGame(PanneauGame pan){
		panGame = pan;
	}

	public void applyPhysics(){
		couleur=Color.black;
		collide();
		if(niveau.getArray().length>((posX+vectorX+panGame.cellSizeX)/panGame.cellSizeX) && ((posX+vectorX)/panGame.cellSizeX)>0)
			posX=posX+vectorX;
		if(niveau.getArray()[1].length>((posY+vectorY+panGame.cellSizeY)/panGame.cellSizeY) && ((posY+vectorY)/panGame.cellSizeY)>0)
			posY=posY+vectorY;
		
		vectorX=vectorX/2;
		vectorY=vectorY/2;

	}

	public void addForceX(double force){
		vectorX=vectorX+force;
	}

	public void addForceY(double force){
		vectorY=vectorY+force;
	}

	public void setPosX(double pos){
		posX=pos;
	}

	public void setPosY(double pos){
		posY=pos;
	}

	public double getPosX(){
		return posX;
	}

	public double getPosY(){
		return posY;
	}
	
	public void setCouleur(Color col){
		couleur=col;
	}

	public Color getCouleur(){
		return couleur;
	}

}
