# Corpus-based lexicalization for DBpedia
The corpus-based lexicalization (CBL) is a tool for bridging the lexical gap between natural language (NL) expressions (i.e. linguistic patterns) and the content stored in an RDF knowledge base (i.e. ontology).  The tool uses class-specific association rules together with null-invariant measures of interestingness to induce correspondences between lexical elements and KB elements. 
- The current version of the tool is implemented for DBpedia. The Corpus-based lexicalization (CBL) is the core task of so-called ontology lexicalization [ontology lexicalization](https://aclanthology.org/W13-3803.pdf). 
- This page provides instructions on how to run the tool.

### Requirements
- docker [docker](https://docs.docker.com/engine/install/)
- 6GB free space: The DBpedia resource includes abstracts, knowledge graph (i.e. triples), and anchor text (i.e. rdfs:label dictionary for entities).The DBpedia resources is provided with the container. The image size is near 5GB .
- - The task is divided into two parts: lexicalization an create lemon. The lexicalization endpoint has to be run first (instruction 3 and 4). After completion, the run create lemon endpoint (instruction 5 and 6).

### Getting started with CBL
To run CBL on your machine, follow 6 instructions given below:

1. Download the image of CBL (lex-cbl). 
```
docker pull elahi/lex-cbl:1.0

```
2. Run the image as a container.
```
docker run -p 8001:8080 -t elahi/lex-cbl:1.0
```

### lexicalization
Given a class, the program will provide class-specific lexicalization. That is, it links the linguistic patterns (a token/a sequence of tokens tagged with parts-of-speech) of the text (of DBpedia abstract) KB elements. <br/>

List of DBpedia class can be found [link](https://github.com/Pret-a-LLOD/ontology-lexicalization/blob/master/classes/classes.txt) <br/>

- For simplicity run it for a single class (for example http://dbpedia.org/ontology/Actor). The input file contains class url and parameteres to run lexicalization process. The detail parameters can be found in [swagger document](https://app.swaggerhub.com/apis/melahi/lex-cbl/1.0.1)

Input Example:
```
{
  "class_url"   : "http://dbpedia.org/ontology/Actor",
  "minimum_entities_per_class": 100,
  "maximum_entities_per_class": 10000,
  "minimum_onegram_length": 4,
  "minimum_pattern_count": 5,
  "minimum_anchor_count": 10,
  "minimum_propertyonegram_length": 4,
  "minimum_propertypattern_count": 5,
  "minimum_propertystring_length": 5,
  "maximum_propertystring_length": 50,
  "minimum_supportA": 5,
  "minimum_supportB": 5,
  "minimum_supportAB":5,
}
```
3. Download the input file
```
wget -O inputLex.json https://raw.githubusercontent.com/Pret-a-LLOD/ontology-lexicalization/master/inputLex.json
```

4. run the following command. The process may take nearly 2 hours.
```
curl -H "Accept: application/json" -H "Content-type: application/json"  --data-binary @inputLex.json -X POST  http://localhost:8001/lexicalization
```
Output Example:
```
{
  "class_url":"http://dbpedia.org/ontology/Actor",
  "status":"Successfull completed lexicalization!!"
}
```
- Note: The system can be also run for all frequent classes (i.e.  340 classes) of DBpedia. It will take more than a week to get results.<br/>

### create lemon
The process will create the results in ontolex lemon format. 
- The input file contains the url (of the resource) and number ranked list (of senses) for each linguistic pattern. the detail can be found in [swagger document](https://app.swaggerhub.com/apis/melahi/lex-cbl/1.0.1)
An example of input file is shown below. 
```
{
  "uri_basic": "http://localhost:8080/",
  "rank_limit": 20
}
```
5. Download input file for lemon creation
```
wget -O inputLemon.json https://raw.githubusercontent.com/Pret-a-LLOD/ontology-lexicalization/master/inputLemon.json
```

6. run the following command
```
curl -H "Accept: application/json" -H "Content-type: application/json"  --data-binary @inputLemon.json -X POST  http://localhost:8001/createLemon
```
- An example of output is [lemon](https://github.com/Pret-a-LLOD/ontology-lexicalization/blob/master/examples/lexicon.json) in Json-LD format.

## CBL code
The project contains code of Perl and Java.


## Reference
Please use the following citation:
```
@inproceedings{Buono-LREC2020,
	title = {{Bridging the gap between Ontology and Lexicon via Class-specific Association Rules Mined from a Loosely-Parallel Text-Data Corpus}},
	author = {Basil Ell, Mohammad Fazleh Elahi, Philipp Cimiano},
	booktitle = {Proceedings of the  3rd Conference on Language, Data and Knowledge (LDK 2021)},
	year = {2021},
	location = {Zaragoza, Spain},
	link = {https://pub.uni-bielefeld.de/record/2954753}
}
```

## Developers
* **Mohammad Fazleh Elahi**
* **Basil Ell**
## Supervisors
* **Dr. Philipp Cimiano**  




---
