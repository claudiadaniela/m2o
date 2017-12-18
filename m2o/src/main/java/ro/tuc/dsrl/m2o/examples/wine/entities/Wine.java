package ro.tuc.dsrl.m2o.examples.wine.entities;

import ro.tuc.dsrl.m2o.annotations.ObjectProperty;
import ro.tuc.dsrl.m2o.annotations.OntologyEntity;
import ro.tuc.dsrl.m2o.annotations.OntologyIgnore;

@OntologyEntity
public class Wine extends PortableLiquid {

	@ObjectProperty(value = "madeFromGrape", range = WineGrape.class)
	private WineGrape wineGrape;

	@ObjectProperty(value = "hasColor", range = WineColor.class)
	private Descriptor color;
	@ObjectProperty(value = "hasBody", range = WineBody.class)
	private Descriptor body;
	@ObjectProperty(value = "hasFlavor", range = WineFlavor.class)
	private Descriptor flavor;
	@ObjectProperty(value = "hasSugar", range = WineSugar.class)
	private Descriptor sugar;
	
	@ObjectProperty(value = "hasMaker", range = Winery.class)
	private Winery maker;
	
	@ObjectProperty(value="locatedIn", range=Region.class)
	private Region location;

	@OntologyIgnore
	private String wineAcidity;
	
	public Wine(){}
	public Wine(long name){super(name);}
	
	public Wine(long name, WineGrape wineGrape, Descriptor color, Descriptor body, Descriptor flavor,
			Descriptor sugar, Winery maker, Region location) {
		super(name);
		this.wineGrape = wineGrape;
		this.color = color;
		this.body = body;
		this.flavor = flavor;
		this.sugar = sugar;
		this.maker = maker;
		this.location = location;
	}

	public WineGrape getWineGrape() {
		return wineGrape;
	}
	public void setWineGrape(WineGrape wineGrape) {
		this.wineGrape = wineGrape;
	}
	
	public Descriptor getColor() {
		return color;
	}

	public void setColor(Descriptor color) {
		this.color = color;
	}

	public Descriptor getBody() {
		return body;
	}

	public void setBody(Descriptor body) {
		this.body = body;
	}

	public Descriptor getFlavor() {
		return flavor;
	}

	public void setFlavor(Descriptor flavor) {
		this.flavor = flavor;
	}

	public Descriptor getSugar() {
		return sugar;
	}

	public void setSugar(Descriptor sugar) {
		this.sugar = sugar;
	}

	public Winery getMaker() {
		return maker;
	}

	public void setMaker(Winery maker) {
		this.maker = maker;
	}

	public Region getLocation() {
		return location;
	}

	public void setLocation(Region location) {
		this.location = location;
	}

	
	
}
