# Corpus-based lexicalization for DBpedia
The Corpus-based lexicalization (CBL) is a tool for bridging the lexical gap between content expressed in the form of natural language (NL) texts and content stored in an RDF knowledge base (KB). The current version of the tool is implemented for DBpedia. The Corpus-based lexicalization (CBL) is the core task of so-called ontology lexicalization [ontology lexicalization](https://aclanthology.org/W13-3803.pdf)

This page provides instructions on how to run the tool.

### Getting started with CBL
To run CBL on your machine follow 5 instrcutions given below:

- docker (https://docs.docker.com/engine/install/)

1. Download the image of CBL. 
```
docker pull pretallod/lex-cbl
```
2. Run the image as a container.
```
docker run -p 8001:8080 -t pretallod/lex-cbl
```
The DBpedia resource including abstracts corpus (i.e. texts), knowledge graph (triples), and anchor text (i.e. rdfs:label dictionary) are very large. Therefore, it is provided inside the container. 

### lexicalization
- Given a DBpedia class, the program will provide class-specific lexicalization. That is, it links the linguistic patterns (a token/a sequence of tokens tagged with parts-of-speech) of the text (of abstract) with predict predicate-object pair, predicate, object, etc.
- The input file contains class and parameteres to run lexicalization process. The detail can be found in [swagger document](https://app.swaggerhub.com/apis/melahi/lex-cbl/1.0.1)

3. Download input file. If you have wget command installed in your terminal, download the configuration file for DBpedia

```
wget -O config.json https://github.com/Pret-a-LLOD/ontology-lexicalization/blob/master/config.json
```
- The lexicalization process includes semantic annotations and association rules.
- The DBpedia is large and so it may take nearly 1 hour to get results for a class. 
- The system can be also run for all frequent classes (frequent 340 classes) of DBpedia but it will take more than a week to get results.

4. run the following command
```
curl -H POST "Accept: application/json" \
     -H "Content-type: application/json" \
     --data-binary @config.json \
     -X POST  http://localhost:8001/lexicalization
```

### create lemon
The process will create the results in ontolex lemon format. 
- The input file contains number ranked list for each linguistic pattern. the detail can be found in [swagger document](https://app.swaggerhub.com/apis/melahi/lex-cbl/1.0.1)
5. run the following command
```
curl -H POST "Accept: application/json" \
     -H "Content-type: application/json" \
     --data-binary @config.json \
     -X POST  http://localhost:8001/createLemon
```

## CBL code
The project contains code of Perl and Java.

## Algorithm behind the project
Given a linguistic pattern (i.e. a token or a sequence of tokens tagged with parts-of-speech and lemmatized), the tool uses class-specific association rules (Ell et al., 2021) together with null-invariant measures of interestingness to predict predicate-object pair (i.e. class lexicalization), predicate, object of DBpedia.

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
