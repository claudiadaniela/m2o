# M2O: A library for using ontologies in software engineering [![DOI](https://zenodo.org/badge/114090358.svg)](https://zenodo.org/badge/latestdoi/114090358)

M2O is an extensible framework over Jena and OWL API that maps complex Java data models onto semantic models based on some custom annotations in order to benefit from the advantages of ontologies in software engineering. Furthermore, it facilitates the implementation of basic CRUD operations for the domain classes and objects, also allowing the definition of new custom operations. 
A more formal description of the framework can be found at http://ieeexplore.ieee.org/document/7312608/

## Using M2O

### M2O Configuration

The setup of this library is done via a configuration file (ont-config.config).  The following parameters need to be setup in order to ensure the correct functionality of the library:

* ONT_FILE= path to the owl file. The ontology file needs to be specified. If the ontology already exists, it will be loaded from this path; otherwise a file will be created containing an ontology corresponding to the Java model - 
* ONT_URI = ontology URI .The identifier of the Ontology (Uniform Resource Identifier) 
* API_TYPE=JENA or OWLAPI. Based on the user preferences, the API needs to be specified in this file. The API’s that are available so far are : JENA and OWL API
* ENTITIES_PACKAGE=ro.tuc.dsrl.m2o.example.entities. The user must specify the package that contains the classes that are going to be mapped to the ontology
* AUTO_GEN=true or false. According to this flag, an ontology will be created based on the entities from the specified package. Otherwise the specified ontology will be loaded from the file.

### Java Model Annotations

Annotated the Java classes that you want to map to the ontology classes. There are four types of annotations used for entity mapping:
@OntologyEntity - It is a class annotation that will be used for every class that needs to be mapped to the ontology. 
@InstanceIdentifier - It is a field annotation specifying the field that will be used to identify individuals. Similarly to the data base approach, this identifier will ensure the uniqueness of the individual and in the same time will allow the user to load the individual based on this identifier.
@ObjectProperty(value = “objProp”, range = Some.class)- This field annotation will be used in order to specify an object property of the current class.  The “value” field will contain the name of the object property and using the “range” field, the class of the range object will be specified.
@OntologyIgnore- In order to ignore a field, this annotation will be used. As a result, this field will not be considered when mapping the class/instance to the ontology class/individual.


```
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
    
    //contructor
    //getters / setters
```

### Java Repositories

Then create the repository classes by extending the OntologyRepository class. The first generic type specifies the entity that the repo is linked to, and the second type specifies the type of individual identifier.

```
public class WineRepository extends OntologyRepository<Wine, Long> {
```
Now, by using an instance of the repository class, one can easily have access to the basic CRUD operations provided by M2O
```
        WineRepository repo = new WineRepository();
        List<Wine> wines = repo.findAll();
```


## Built With
* [Maven](https://maven.apache.org/) - Dependency Management

## Authors

* Claudia Pop ;  Dorin Moldovan ;  Marcel Antal ;  Dan Valea ;  Tudor Cioara ;  Ionut Anghel ;  Ioan Salomie

See also the list of [contributors](https://github.com/claudiadaniela/m2o/graphs/contributors) who participated in this project.

## License

This project is licensed under the GNL License - see the [LICENSE](LICENSE) file for details

