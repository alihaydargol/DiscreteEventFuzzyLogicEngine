package main.java.engine.fuzzytoolkit;

import java.util.HashMap;

public class FuzzyControlSystem {
	
	private FuzzySolution solution;
	
	public FuzzyControlSystem() {
		
	}
	
	
}

//float qual_crisp = 6.5f;
//float serv_crisp = 9.8f;
//
//float[] x_qual = FuzzyMath.linspace(0, 10, 10);
//float[] x_serv = FuzzyMath.linspace(0, 10, 10);
//float[] x_tip = FuzzyMath.linspace(0, 25, 25);
//
//float [] qual_lo = Membership.trimf(x_qual, 0, 0, 5);
//float [] qual_md = Membership.trimf(x_qual, 0, 5, 10);
//float [] qual_hi = Membership.trimf(x_qual, 5, 10, 10);
//
//float [] serv_lo = Membership.trimf(x_serv, 0, 0, 5);
//float [] serv_md = Membership.trimf(x_serv, 0, 5, 10);
//float [] serv_hi = Membership.trimf(x_serv, 5, 10, 10);
//		
//float [] tip_lo = Membership.trimf(x_tip, 0, 0, 13);
//float [] tip_md = Membership.trimf(x_tip, 0, 13, 25);
//float [] tip_hi = Membership.trimf(x_tip, 13, 25, 25);
//				
//float qual_level_lo = FuzzyOperations.interpolateMembership(x_qual, qual_lo, qual_crisp, true);
//float qual_level_md = FuzzyOperations.interpolateMembership(x_qual, qual_md, qual_crisp, true);
//float qual_level_hi = FuzzyOperations.interpolateMembership(x_qual, qual_hi, qual_crisp, true);
//
//float serv_level_lo = FuzzyOperations.interpolateMembership(x_serv, serv_lo, serv_crisp, true);
//float serv_level_md = FuzzyOperations.interpolateMembership(x_serv, serv_md, serv_crisp, true);
//float serv_level_hi = FuzzyOperations.interpolateMembership(x_serv, serv_hi, serv_crisp, true);
//
//float active_rule1 = FuzzyMath.fmax(qual_level_lo, serv_level_lo);
//
//float[] tip_activation_lo = FuzzyMath.fmin(active_rule1, tip_lo);
//float[] tip_activation_md = FuzzyMath.fmin(serv_level_md, tip_md);
//
//float active_rule3 = FuzzyMath.fmax(qual_level_hi, serv_level_hi);
//
//float[] tip_activation_hi = FuzzyMath.fmin(active_rule3, tip_hi);
//
//float[] aggregated = FuzzyMath.fmax(tip_activation_lo, FuzzyMath.fmax(tip_activation_md, tip_activation_hi));
//
//float tip = FuzzyOperations.defuzz(x_tip, aggregated, "centroid");