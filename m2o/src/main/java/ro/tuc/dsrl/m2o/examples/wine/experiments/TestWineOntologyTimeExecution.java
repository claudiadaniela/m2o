package ro.tuc.dsrl.m2o.examples.wine.experiments;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ro.tuc.dsrl.m2o.examples.wine.entities.Region;
import ro.tuc.dsrl.m2o.examples.wine.entities.Wine;
import ro.tuc.dsrl.m2o.examples.wine.entities.WineBody;
import ro.tuc.dsrl.m2o.examples.wine.entities.WineColor;
import ro.tuc.dsrl.m2o.examples.wine.entities.WineFlavor;
import ro.tuc.dsrl.m2o.examples.wine.entities.WineSugar;
import ro.tuc.dsrl.m2o.examples.wine.entities.Winery;
import ro.tuc.dsrl.m2o.examples.wine.repos.RegionRepository;
import ro.tuc.dsrl.m2o.examples.wine.repos.WineBodyRepository;
import ro.tuc.dsrl.m2o.examples.wine.repos.WineColorRepository;
import ro.tuc.dsrl.m2o.examples.wine.repos.WineFlavorRepository;
import ro.tuc.dsrl.m2o.examples.wine.repos.WineRepository;
import ro.tuc.dsrl.m2o.examples.wine.repos.WineSugarRepository;
import ro.tuc.dsrl.m2o.examples.wine.repos.WineryRepository;
import ro.tuc.dsrl.m2o.ontology.utility.OntologyUtilityFactory;

import java.util.Date;

public class TestWineOntologyTimeExecution {
	private static final Log LOGGER = LogFactory.getLog(TestWineOntologyTimeExecution.class);
	private static final int NO_ENTITIES = 10000;

	private TestWineOntologyTimeExecution() {
	}

	public static void main(String[] args) {

		insertWine();
		OntologyUtilityFactory.getInstance().saveSnapshot(new Date());
		getEntities();
	}

	private static void getEntities() {
		WineRepository repo = new WineRepository();
		long startTime = System.nanoTime();
		repo.findAll();
		long endTime = System.nanoTime();

		long duration1 = (endTime - startTime);
		System.out.println("Retrival time is: " + duration1 / 1000000);
	}

	private static void insertWine() {
		WineryRepository wineryRepository = new WineryRepository();
		Winery winery = new Winery(1);
		wineryRepository.create(winery);
		LOGGER.info("Winery created ");

		Region region = new Region(1);
		RegionRepository regionRepository = new RegionRepository();
		regionRepository.create(region);
		LOGGER.info("Region created ");

		WineBodyRepository bodyRepository = new WineBodyRepository();
		WineBody wBody = new WineBody(1L);
		bodyRepository.create(wBody);
		LOGGER.info("WineBody created ");

		WineColorRepository colorRepository = new WineColorRepository();
		WineColor wColor = new WineColor(1L);
		colorRepository.create(wColor);
		LOGGER.info("WineColor created ");

		WineSugarRepository sugarReporsitory = new WineSugarRepository();
		WineSugar wSugar = new WineSugar(1);
		sugarReporsitory.create(wSugar);
		LOGGER.info("WineSugar created ");

		WineFlavorRepository flavorRepository = new WineFlavorRepository();
		WineFlavor wFlavor = new WineFlavor(1);
		flavorRepository.create(wFlavor);
		LOGGER.info("WineFlavor created ");

		WineRepository repo = new WineRepository();
		long startTime = System.nanoTime();
		for (int i = 0; i < NO_ENTITIES; i++) {
			Wine wine = new Wine(i);
			wine.setBody(wBody);
			wine.setColor(wColor);
			wine.setFlavor(wFlavor);
			wine.setLocation(region);
			wine.setMaker(winery);
			wine.setSugar(wSugar);
			repo.create(wine);
		}

		long endTime = System.nanoTime();

		long duration1 = (endTime - startTime);
		System.out.println("Insertion time for : " + NO_ENTITIES + " entities is  " + duration1 / 1000000);
	}

	private static void createWine() {
		WineryRepository wineryRepository = new WineryRepository();
		Winery winery = new Winery(1);
		wineryRepository.create(winery);
		LOGGER.info("Winery created ");

		Region region = new Region(1);
		RegionRepository regionRepository = new RegionRepository();
		regionRepository.create(region);
		LOGGER.info("Region created ");

		WineBodyRepository bodyRepository = new WineBodyRepository();
		WineBody wBody = new WineBody(1L);
		bodyRepository.create(wBody);
		LOGGER.info("WineBody created ");

		WineColorRepository colorRepository = new WineColorRepository();
		WineColor wColor = new WineColor(1L);
		colorRepository.create(wColor);
		LOGGER.info("WineColor created ");

		WineSugarRepository sugarReporsitory = new WineSugarRepository();
		WineSugar wSugar = new WineSugar(1);
		sugarReporsitory.create(wSugar);
		LOGGER.info("WineSugar created ");

		WineFlavorRepository flavorRepository = new WineFlavorRepository();
		WineFlavor wFlavor = new WineFlavor(1);
		flavorRepository.create(wFlavor);
		LOGGER.info("WineFlavor created ");

		LOGGER.info("Winecreated ");
	}
}
