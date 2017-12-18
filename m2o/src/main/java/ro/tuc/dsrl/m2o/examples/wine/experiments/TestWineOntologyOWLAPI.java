package ro.tuc.dsrl.m2o.examples.wine.experiments;

import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import ro.tuc.dsrl.m2o.ontology.utility.OwlAPIUtility;
import ro.tuc.dsrl.m2o.examples.wine.entities.Region;
import ro.tuc.dsrl.m2o.examples.wine.entities.Wine;
import ro.tuc.dsrl.m2o.examples.wine.entities.WineBody;
import ro.tuc.dsrl.m2o.examples.wine.entities.WineColor;
import ro.tuc.dsrl.m2o.examples.wine.entities.WineFlavor;
import ro.tuc.dsrl.m2o.examples.wine.entities.WineGrape;
import ro.tuc.dsrl.m2o.examples.wine.entities.WineSugar;
import ro.tuc.dsrl.m2o.examples.wine.entities.Winery;

public class TestWineOntologyOWLAPI {

	private final static String NAMESPACE = "http://www.w3.org/TR/2003/PR-owl-guide-20031209/wine#";

	private TestWineOntologyOWLAPI() {
	}

	private static void createWineIndividual(OwlAPIUtility instance) {
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

		OWLDataFactory factory = instance.getFactory();
		OWLOntologyManager manager = instance.getManager();
		OWLOntology ontology = instance.getOntology();

		// create an individual of type Wine

		OWLClass wineClass = factory.getOWLClass(IRI.create(NAMESPACE + "Wine"));
		OWLNamedIndividual wineIndividual = factory.getOWLNamedIndividual(IRI.create(NAMESPACE + "Wine" + "_"
				+ wine.getName()));
		OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(wineClass, wineIndividual);
		manager.addAxiom(ontology, classAssertion);
		OWLDataProperty hasId = factory.getOWLDataProperty(IRI.create(NAMESPACE + "hasId"));
		OWLDataPropertyAssertionAxiom hasIdAssertionAxiom = factory.getOWLDataPropertyAssertionAxiom(hasId,
				wineIndividual, wine.getName());
		manager.addAxiom(ontology, hasIdAssertionAxiom);

		// associate the Wine individual with a WineGrape individual

		OWLObjectProperty madeFromGrape = factory.getOWLObjectProperty(IRI.create(NAMESPACE + "madeFromGrape"));
		OWLNamedIndividual wineGrapeIndividual = factory.getOWLNamedIndividual(IRI.create(NAMESPACE + "WineGrape" + "_"
				+ wine.getWineGrape().getId()));
		OWLObjectPropertyAssertionAxiom madeFromGrapeAssertionAxiom = factory.getOWLObjectPropertyAssertionAxiom(
				madeFromGrape, wineIndividual, wineGrapeIndividual);
		manager.addAxiom(ontology, madeFromGrapeAssertionAxiom);

		// associate the Wine individual with a WineColor individual

		OWLObjectProperty hasColor = factory.getOWLObjectProperty(IRI.create(NAMESPACE + "hasColor"));
		OWLNamedIndividual wineColorIndividual = factory.getOWLNamedIndividual(IRI.create(NAMESPACE + "WineColor" + "_"
				+ wine.getColor().getId()));
		OWLObjectPropertyAssertionAxiom hasColorAssertionAxiom = factory.getOWLObjectPropertyAssertionAxiom(hasColor,
				wineIndividual, wineColorIndividual);
		manager.addAxiom(ontology, hasColorAssertionAxiom);

		// associate the Wine individual with a WineBody individual

		OWLObjectProperty hasBody = factory.getOWLObjectProperty(IRI.create(NAMESPACE + "hasBody"));
		OWLNamedIndividual wineBodyIndividual = factory.getOWLNamedIndividual(IRI.create(NAMESPACE + "WineBody" + "_"
				+ wine.getBody().getId()));
		OWLObjectPropertyAssertionAxiom hasBodyAssertionAxiom = factory.getOWLObjectPropertyAssertionAxiom(hasBody,
				wineIndividual, wineBodyIndividual);
		manager.addAxiom(ontology, hasBodyAssertionAxiom);

		// associate the Wine individual with a WineFlavor individual

		OWLObjectProperty hasFlavor = factory.getOWLObjectProperty(IRI.create(NAMESPACE + "hasFlavor"));
		OWLNamedIndividual wineFlavorIndividual = factory.getOWLNamedIndividual(IRI.create(NAMESPACE + "WineFlavor"
				+ "_" + wine.getFlavor().getId()));
		OWLObjectPropertyAssertionAxiom hasFlavorAssertionAxiom = factory.getOWLObjectPropertyAssertionAxiom(hasFlavor,
				wineIndividual, wineFlavorIndividual);
		manager.addAxiom(ontology, hasFlavorAssertionAxiom);

		// associate the Wine individual with a WineSugar individual

		OWLObjectProperty hasSugar = factory.getOWLObjectProperty(IRI.create(NAMESPACE + "hasSugar"));
		OWLNamedIndividual wineSugarIndividual = factory.getOWLNamedIndividual(IRI.create(NAMESPACE + "WineSugar" + "_"
				+ wine.getSugar().getId()));
		OWLObjectPropertyAssertionAxiom hasSugarAssertionAxiom = factory.getOWLObjectPropertyAssertionAxiom(hasSugar,
				wineIndividual, wineSugarIndividual);
		manager.addAxiom(ontology, hasSugarAssertionAxiom);

		// associate the Wine individual with a Winery individual

		OWLObjectProperty hasMaker = factory.getOWLObjectProperty(IRI.create(NAMESPACE + "hasMaker"));
		OWLNamedIndividual wineryIndividual = factory.getOWLNamedIndividual(IRI.create(NAMESPACE + "Winery" + "_"
				+ wine.getMaker().getId()));
		OWLObjectPropertyAssertionAxiom hasMakerAssertionAxiom = factory.getOWLObjectPropertyAssertionAxiom(hasMaker,
				wineIndividual, wineryIndividual);
		manager.addAxiom(ontology, hasMakerAssertionAxiom);

		// associate the Wine individual with a Region individual

		OWLObjectProperty locatedIn = factory.getOWLObjectProperty(IRI.create(NAMESPACE + "locatedIn"));
		OWLNamedIndividual regionIndividual = factory.getOWLNamedIndividual(IRI.create(NAMESPACE + "Region" + "_"
				+ wine.getLocation().getId()));
		OWLObjectPropertyAssertionAxiom locatedInAssertionAxiom = factory.getOWLObjectPropertyAssertionAxiom(locatedIn,
				wineIndividual, regionIndividual);
		manager.addAxiom(ontology, locatedInAssertionAxiom);
	}

	private static Wine getWineIndividual(OwlAPIUtility instance, long id) {

		OWLDataFactory factory = instance.getFactory();
		OWLOntologyManager manager = instance.getManager();
		OWLOntology ontology = instance.getOntology();

		Wine wine = new Wine();

		wine.setName(id);

		OWLNamedIndividual individual = factory.getOWLNamedIndividual(IRI.create(NAMESPACE + "Wine" + "_"
				+ wine.getName()));

		// OWLDataProperty hasId =
		// factory.getOWLDataProperty(IRI.create(NAMESPACE + "hasId"))

		// Get the WineGrape

		// OWLObjectProperty madeFromGrape =
		// factory.getOWLObjectProperty(IRI.create(NAMESPACE + "madeFromGrape"))

		Map<OWLObjectPropertyExpression, Set<OWLIndividual>> objectPropertyValues = individual
				.getObjectPropertyValues(ontology);

		for (OWLObjectPropertyExpression opv : objectPropertyValues.keySet()) {

			Set<OWLIndividual> newIndiv = objectPropertyValues.get(opv);
			String propName = opv.asOWLObjectProperty().getIRI().getFragment();

			Long objectId = null;

			for (OWLIndividual owlIndividual : newIndiv) {

				Map<OWLDataPropertyExpression, Set<OWLLiteral>> dataPropertyValues = owlIndividual
						.getDataPropertyValues(ontology);

				for (OWLDataPropertyExpression dpv : dataPropertyValues.keySet()) {

					Set<OWLLiteral> literalSet = dataPropertyValues.get(dpv);

					for (OWLLiteral literal : literalSet) {
						objectId = Long.parseLong(literal.getLiteral());
					}

				}
			}

			if (propName.equals("hasSugar")) {
				WineSugar sugar = new WineSugar(id);
				wine.setSugar(sugar);
			} else if (propName.equals("madeFromGrape")) {
				WineGrape wineGrape = new WineGrape(id);
				wine.setWineGrape(wineGrape);
			} // the same for the other cases
		}

		return wine;
	}

	public static void main(String[] args) {
		OwlAPIUtility instance = OwlAPIUtility.getInstance();

		// CREATE

		TestWineOntologyOWLAPI.createWineIndividual(instance);

		// FIND BY ID

		TestWineOntologyOWLAPI.getWineIndividual(instance, 1L);

		// instance.saveSnapshot(new Date
	}

}
