package main.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import peasy.PeasyCam;
import peasy.PeasyDragHandler;
import processing.core.PApplet;

public class HUD {
	
	private HashMap<String, List<float []>> discreteFunctions = new HashMap<String, List<float []>>();
	private HashMap<String, float []> discreteFunctionsInputs = new HashMap<String, float []>();
	
	private float figureScale = 1.2f;
	private float inputRadius = 5;
	
	private int windowHeigth;
	private int windowWidth;
	
	private int widthOffset;
	private int heigthOffset;
	
	private int xAxisLength = (int) (150 * figureScale);
	private int yAxisLength = xAxisLength / 2;
		
	PApplet applet = null;
	PeasyCam camera = null;
	
	public HUD(PApplet applet, PeasyCam camera, int windowWidth, int windowHeigth) {
		this.applet = applet;
		this.camera = camera;
		this.windowWidth = windowWidth;
		this.windowHeigth = windowHeigth;
		
		widthOffset = windowWidth - xAxisLength - (int)(xAxisLength * 0.33);
		heigthOffset = (int)(yAxisLength * 0.33);
	}
	
	public void registerDiscreteFunction(String name, List<float []> functions) {
		discreteFunctions.put(name, functions);
	}
	
	public int drawDiscreteFunction(String key, List<float []> functions, int xGrid, int yGrid, float [] x) {
	
		int functionXOffset = widthOffset - (int)Math.ceil(xAxisLength * 1.50) * xGrid; // TODO : Test xGrid
		int functionYOffset = heigthOffset + (int)Math.ceil(yAxisLength * 1.50) * yGrid;
		
		applet.fill(0, 255, 0);
		applet.stroke(0, 255, 0);
		
		applet.textSize(xAxisLength / 12);
		
		applet.line(functionXOffset, functionYOffset + yAxisLength, functionXOffset + xAxisLength, functionYOffset + yAxisLength);
		applet.text(key, functionXOffset + xAxisLength, functionYOffset + yAxisLength + 15 * figureScale);
		
		applet.line(functionXOffset + xAxisLength / 2, functionYOffset, functionXOffset + xAxisLength / 2, functionYOffset + yAxisLength);
		applet.text("u", functionXOffset + xAxisLength / 2 + 5, functionYOffset);
				
		float[] domain = functions.get(0);
		float domain_range = domain[domain.length - 1] - domain[0];
		
		applet.textSize(xAxisLength / 18);
		
		for (int i = 1; i < functions.size(); i++) {
			float[] function = functions.get(i);
						
			for (int j = 1; j < function.length; j++) {
				applet.line(functionXOffset + xAxisLength * 0.05f + (domain[j - 1] / domain_range) * xAxisLength * 0.9f, 
							functionYOffset + yAxisLength * 0.2f + (1 - function[j - 1]) * yAxisLength * 0.8f, 
							functionXOffset + xAxisLength * 0.05f + (domain[j] / domain_range) * xAxisLength * 0.9f, 
							functionYOffset + yAxisLength * 0.2f + (1 - function[j]) * yAxisLength * 0.8f);
							
				// applet.text(Integer.toString((int)domain[j - 1]), functionXOffset + xAxisLength * 0.05f + (domain[j - 1] / domain_range) * xAxisLength * 0.9f, functionYOffset + yAxisLength + 8 * figureScale);
			}
			// applet.text(Integer.toString((int)domain[domain.length - 1]), functionXOffset + xAxisLength * 0.05f + (domain[domain.length - 1] / domain_range) * xAxisLength * 0.9f, functionYOffset + yAxisLength + 8 * figureScale);
		}
		
		applet.fill(255, 0, 0);
		applet.stroke(255, 0, 0);
		
		applet.circle(functionXOffset + xAxisLength * 0.05f + (x[0] / domain_range) * xAxisLength * 0.9f, 
				functionYOffset + yAxisLength, inputRadius);
		
		for (int i = 1; i < x.length; i++)
			applet.circle(functionXOffset + xAxisLength * 0.05f + (x[0] / domain_range) * xAxisLength * 0.9f, 
					functionYOffset + yAxisLength* 0.2f + (1 - x[i]) * yAxisLength * 0.8f, 
					inputRadius);	
		
		return functionYOffset = heigthOffset + (int)Math.ceil(yAxisLength * 1.50) * yGrid + 1;
	}
	
	public void drawDiscreteFunctions(String key) {
		
		camera.beginHUD();
		
		if (key == "all") {

			int counterX = 0;
			int counterY = 0;
			
			Iterator<Entry<String, List<float []>>> discreteFunctionsIterator = this.discreteFunctions.entrySet().iterator();
			Iterator<Entry<String, float []>> discreteFunctionsInputsIterator = this.discreteFunctionsInputs.entrySet().iterator();
			
			while (discreteFunctionsIterator.hasNext() && discreteFunctionsInputsIterator.hasNext()) {
		        Map.Entry<String, List<float []>> functionPair = (Map.Entry<String, List<float []>>)discreteFunctionsIterator.next();
		        Map.Entry<String, float []> inputPair = (Map.Entry<String, float []>)discreteFunctionsInputsIterator.next();
			        		        
		        int nextYOffset = drawDiscreteFunction(functionPair.getKey(), functionPair.getValue(), counterX, counterY, inputPair.getValue());
		        
		        if (nextYOffset > windowHeigth) {
		        	counterX++;
		        	counterY = 0;
		        }
		        
		        counterY++;
			}
		}
		
		camera.endHUD();
	}
	
	public void updateDiscreteFunctionsInputs(HashMap<String, float[]> updatedDiscreteFunctionsInputs) {discreteFunctionsInputs.putAll(updatedDiscreteFunctionsInputs);}
	
	public void setScale(float scale) {this.figureScale = scale;};
	
}
