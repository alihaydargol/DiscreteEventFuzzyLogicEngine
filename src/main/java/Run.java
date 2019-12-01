package main.java;

import processing.core.PApplet;
import processing.core.PVector;
import peasy.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import main.java.app.agents.Forklift;
import main.java.app.agents.Rover;
import main.java.engine.fuzzytoolkit.FuzzyMath;
import main.java.engine.fuzzytoolkit.FuzzyOperations;
import main.java.engine.fuzzytoolkit.Membership;
import main.java.space.Environment;
import main.java.space.Workspace;
import main.java.space.WorkspaceBuilder;
import main.java.space.WorkspaceFactory;

public class Run extends PApplet {
	
	int window_width = 1280;
	int window_heigth = 768;

	PeasyCam camera;
	HUD hud;
	
	Rover rover;
	Forklift forklift;
	
	Workspace warehouse;

	float[][] terrain;
	
	float qual_crisp = 6.5f;
	float serv_crisp = 9.8f;

	float[] x_qual = FuzzyMath.linspace(0, 10, 10);
	float[] x_serv = FuzzyMath.linspace(0, 10, 10);
	float[] x_tip = FuzzyMath.linspace(0, 25, 25);
	
	float [] qual_lo = Membership.trimf(x_qual, 0, 0, 5);
	float [] qual_md = Membership.trimf(x_qual, 0, 5, 10);
	float [] qual_hi = Membership.trimf(x_qual, 5, 10, 10);

	float [] serv_lo = Membership.trimf(x_serv, 0, 0, 5);
	float [] serv_md = Membership.trimf(x_serv, 0, 5, 10);
	float [] serv_hi = Membership.trimf(x_serv, 5, 10, 10);
			
	float [] tip_lo = Membership.trimf(x_tip, 0, 0, 13);
	float [] tip_md = Membership.trimf(x_tip, 0, 13, 25);
	float [] tip_hi = Membership.trimf(x_tip, 13, 25, 25);
					
	float qual_level_lo = FuzzyOperations.interpolateMembership(x_qual, qual_lo, qual_crisp, true);
	float qual_level_md = FuzzyOperations.interpolateMembership(x_qual, qual_md, qual_crisp, true);
	float qual_level_hi = FuzzyOperations.interpolateMembership(x_qual, qual_hi, qual_crisp, true);

	float serv_level_lo = FuzzyOperations.interpolateMembership(x_serv, serv_lo, serv_crisp, true);
	float serv_level_md = FuzzyOperations.interpolateMembership(x_serv, serv_md, serv_crisp, true);
	float serv_level_hi = FuzzyOperations.interpolateMembership(x_serv, serv_hi, serv_crisp, true);
	
	float active_rule1 = FuzzyMath.fmax(qual_level_lo, serv_level_lo);
	
	float[] tip_activation_lo = FuzzyMath.fmin(active_rule1, tip_lo);
	float[] tip_activation_md = FuzzyMath.fmin(serv_level_md, tip_md);
	
	float active_rule3 = FuzzyMath.fmax(qual_level_hi, serv_level_hi);
	
	float[] tip_activation_hi = FuzzyMath.fmin(active_rule3, tip_hi);
	
	float[] tip0 = FuzzyMath.zeros_like(x_tip);
	
	float[] aggregated = FuzzyMath.fmax(tip_activation_lo, FuzzyMath.fmax(tip_activation_md, tip_activation_hi));
	
	float tip = FuzzyOperations.defuzz(x_tip, aggregated, "centroid");

	public static void main(String[] args) {
		PApplet.main("main.java.Run");
	}

	public void settings() {
		size(window_width, window_heigth, P3D);
	}

	public void setup() {
		
		WorkspaceFactory workspaceFactory = new WorkspaceFactory();
		
		WorkspaceBuilder workspaceBuilder = new WorkspaceBuilder(this);
		workspaceBuilder.setWorkspaceFactory(workspaceFactory);
		
		workspaceBuilder.setWorkspace("warehouse");
		workspaceBuilder.configure();
		
		warehouse = workspaceBuilder.getWorkspace();
		
		terrain = warehouse.terrainFactory();

		rover = new Rover(this, new PVector(-0.0f, 0, terrain[15][15]), new PVector(0, 0, 0));
		// rover.toggleManualDriving();
		// rover.toggleFirstPersonCamera();

		forklift = new Forklift(this, new PVector(-0.0f, 0, terrain[18][18]), new PVector(0, 0, 0));
		forklift.toggleManualDriving();
		// rover.toggleFirstPersonCamera();

		camera = new PeasyCam(this, 250);
		
		hud = new HUD(this, camera, window_width, window_heigth);
		
		HashMap<String, float []> crips = new HashMap<String, float []>();
		
		crips.put("x_qual", new float[] {qual_crisp, qual_level_lo, qual_level_md, qual_level_hi});
		crips.put("x_serv", new float[] {serv_crisp, serv_level_lo, serv_level_md, serv_level_hi});
		
		List<float[]> qual = new ArrayList<float[]>();
		List<float[]> serv = new ArrayList<float[]>();
		
		qual.add(x_qual);
		qual.add(qual_lo);
		qual.add(qual_md);
		qual.add(qual_hi);
		
		serv.add(x_serv);
		serv.add(serv_lo);
		serv.add(serv_md);
		serv.add(serv_hi);
		
		hud.registerDiscreteFunction("x_qual", qual);
		hud.registerDiscreteFunction("x_serv", serv);
		
		hud.updateDiscreteFunctionsInputs(crips);
		
		frameRate(30);
		fill(120, 50, 240);
		noStroke();
		
		System.out.println(tip);
				
	}

	public void draw() {
		background(0);

		warehouse.draw();
		warehouse.drawGuideLines();
		
		warehouse.simulate();
								
		forklift.operate();
		forklift.update();
		forklift.draw();
		
		rover.setTarget(forklift.getPosition());
		
		rover.operate();
		rover.update();
		rover.draw();
		
		hud.drawDiscreteFunctions("all");
	}
}

