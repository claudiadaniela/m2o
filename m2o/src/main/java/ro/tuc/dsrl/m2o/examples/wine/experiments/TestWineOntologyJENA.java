package ro.tuc.dsrl.m2o.examples.wine.experiments;

import ro.tuc.dsrl.m2o.ontology.utility.JenaUtility;
import ro.tuc.dsrl.m2o.examples.wine.entities.Region;
import ro.tuc.dsrl.m2o.examples.wine.entities.Wine;
import ro.tuc.dsrl.m2o.examples.wine.entities.WineBody;
import ro.tuc.dsrl.m2o.examples.wine.entities.WineColor;
import ro.tuc.dsrl.m2o.examples.wine.entities.WineFlavor;
import ro.tuc.dsrl.m2o.examples.wine.entities.WineGrape;
import ro.tuc.dsrl.m2o.examples.wine.entities.WineSugar;
import ro.tuc.dsrl.m2o.examples.wine.entities.Winery;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.rdf.model.NodeIterator;

public class TestWineOntologyJENA {

	private final static String NAMESPACE = "http://www.w3.org/TR/2003/PR-owl-guide-20031209/wine#";

	private TestWineOntologyJENA() {
	}

	private static void createWineIndividual(JenaUtility instance) {
		Wine wine = new Wine(1L);
		WineGrape wineGrape = new WineGrape(1L);
		WineColor wineColor = new WineColor(1L);
		WineBody wineBody = new WineBody(1L);
		WineFlavor wineFlavor = new WineFlavor(1L);
		WineSugar wineSugar = new WineSugar(1L);
		Winery maker = new Winery(1L);
		Region region = new Region(1L);

		wine.setWineGrape(wineGrape);
		wine.setColor(wineColor);
		wine.setBody(wineBody);
		wine.setFlavor(wineFlavor);
		wine.setSugar(wineSugar);
		wine.setMaker(maker);
		wine.setLocation(region);

		// create an individual of type Wine

		OntClass wineClass = instance.getOntModel().getOntClass(NAMESPACE + "Wine");
		Individual wineIndividual = instance.getOntModel().createIndividual(NAMESPACE + "Wine" + "_" + wine.getName(),
				wineClass);
		DatatypeProperty datatypeProperty = instance.getOntModel().getDatatypeProperty(NAMESPACE + "hasId");
		wineIndividual.addProperty(datatypeProperty, new Long(wine.getName()).toString(), XSDDatatype.XSDlong);

		// associate the Wine individual with a WineGrape individual

		Individual wineGrapeIndividual = instance.getOntModel().getIndividual(
				NAMESPACE + "WineGrape" + "_" + wine.getWineGrape().getId());
		ObjectProperty madeFromGrape = instance.getOntModel().getObjectProperty(NAMESPACE + "madeFromGrape");
		wineIndividual.addProperty(madeFromGrape, wineGrapeIndividual);

		// associate the Wine individual with a WineColor individual

		Individual wineColorIndividual = instance.getOntModel().getIndividual(
				NAMESPACE + "WineColor" + "_" + wine.getColor().getId());
		ObjectProperty hasColor = instance.getOntModel().getObjectProperty(NAMESPACE + "hasColor");
		wineIndividual.addProperty(hasColor, wineColorIndividual);

		// associate the Wine individual with a WineBody individual

		Individual wineBodyIndividual = instance.getOntModel().getIndividual(
				NAMESPACE + "WineBody" + "_" + wine.getBody().getId());
		ObjectProperty hasBody = instance.getOntModel().getObjectProperty(NAMESPACE + "hasBody");
		wineIndividual.addProperty(hasBody, wineBodyIndividual);

		// associate the Wine individual with a WineFlavor individual

		Individual wineFlavorIndividual = instance.getOntModel().getIndividual(
				NAMESPACE + "WineFlavor" + "_" + wine.getFlavor().getId());
		ObjectProperty hasFlavor = instance.getOntModel().getObjectProperty(NAMESPACE + "hasFlavor");
		wineIndividual.addProperty(hasFlavor, wineFlavorIndividual);

		// associate the Wine individual with a WineSugar individual

		Individual wineSugarIndividual = instance.getOntModel().getIndividual(
				NAMESPACE + "WineSugar" + "_" + wine.getSugar().getId());
		ObjectProperty hasSugar = instance.getOntModel().getObjectProperty(NAMESPACE + "hasSugar");
		wineIndividual.addProperty(hasSugar, wineSugarIndividual);

		// associate the Wine individual with a Winery individual

		Individual wineryIndividual = instance.getOntModel().getIndividual(
				NAMESPACE + "Winery" + "_" + wine.getMaker().getId());
		ObjectProperty hasMaker = instance.getOntModel().getObjectProperty(NAMESPACE + "hasMaker");
		wineIndividual.addProperty(hasMaker, wineryIndividual);

		// associate the Wine individual with a Region individual

		Individual regionIndividual = instance.getOntModel().getIndividual(
				NAMESPACE + "Region" + "_" + wine.getLocation().getId());
		ObjectProperty locatedIn = instance.getOntModel().getObjectProperty(NAMESPACE + "locatedIn");
		wineIndividual.addProperty(locatedIn, regionIndividual);
	}

	private static Wine getWineIndividual(JenaUtility instance, long id) {
		Wine wine = new Wine();

		wine.setName(id);

		Individual wineIndividual = instance.getOntModel().getIndividual(NAMESPACE + "Wine" + "_" + 1);

		DatatypeProperty hasId = instance.getOntModel().getDatatypeProperty(NAMESPACE + "hasId");

		// Get the WineGrape

		ObjectProperty madeFromGrape = instance.getOntModel().getObjectProperty(NAMESPACE + "madeFromGrape");

		NodeIterator iterator = instance.getOntModel().listObjectsOfProperty(wineIndividual, madeFromGrape);

		while (iterator.hasNext()) {
			Individual individual = instance.getOntModel().getIndividual(iterator.next().toString());
			long wineGrapeId = individual.getProperty(hasId).getLong();
			WineGrape wineGrape = new WineGrape(wineGrapeId);
			wine.setWineGrape(wineGrape);
		}

		// Get the WineColor

		ObjectProperty hasColor = instance.getOntModel().getObjectProperty(NAMESPACE + "hasColor");

		iterator = instance.getOntModel().listObjectsOfProperty(wineIndividual, hasColor);

		while (iterator.hasNext()) {
			Individual individual = instance.getOntModel().getIndividual(iterator.next().toString());
			long wineColorId = individual.getProperty(hasId).getLong();
			WineColor wineColor = new WineColor(wineColorId);
			wine.setColor(wineColor);
		}

		// The same number of lines of code for body, flavor, sugar, winery,
		// region

		return wine;
	}

	public static void main(String[] args) {

		JenaUtility instance = JenaUtility.getInstance();

		// CREATE

		TestWineOntologyJENA.createWineIndividual(instance);

		// FIND BY ID

		TestWineOntologyJENA.getWineIndividual(instance, 1L);

		// instance.saveSnapshot(new Date())
	}

}
