
Stages
=====================



As described in the overview of the :doc:`pipeline <pipeline>`, ArDoCo is subdivided in different stages.
The stages themselves can be exchanged, but have dependencies on the previous stages of the pipeline.


.. image:: images/pipeline2.png
    :alt: center


The agile design enables to rearrange or replace each step of the pipeline.
Thereby, the parts of it can be exchanged or reused in other ways.



After the initial *data structure* was created, the other steps of the pipeline are executed.
Every stage is executed as an *execution stage*.
Every *execution stage* uses different *agents* to fulfill its function.

Every agent has a specific goal (e.g. the detection of named entities).
To fulfill this task an agent can have multiple *extractors*.
Extractors run over the annotated text and extract needed information for the current stage.

In contrast to them, general *agents* base only on the extracted data of previous stages, but do not go over the whole text.
However, some stages (esp. the text extraction) need to look on the preprocessed text.
Therefore, these stages have agents that contain extractors.
To ensure that these extractors run before every other (maybe) on their result basing agents, they are commonly named *initialAgent* and executed before any other agents.

.. .. hint:: This should be refactored for the 0.4 release

Text Extraction
----------------------------------
The :doc:`text extractor<stages/textExtraction>` depends on the annotated input text.
It uses PosTags, DependencyTags, and other information, as described in the :doc:`preprocessing section <stages/textPreprocessing>`
Currently most agents of this stage use heuristics to detect possible instances and relations.

The extracted information are stored in the :doc:`text extraction state <data/textextractionData>`.

The text extraction should thin out unnecessary information by extracting all possibly relevant instances and relations.
However, since the later steps rely on this step, the recall of this step should be kept high, whereas a high precision is less important.


Recommendation Generation
----------------------------------

The :doc:`recommendation generator <stages/recommendationGeneration>` requires the :doc:`annotated text <data/textData>`, the :doc:`extracted text data <data/textData>`, and the :doc:`metamodel <data/modelData>`.
The goal of the recommendation generation is to build potential elements that could occur in the model.
The properties of the elements depend on the underlying meta model (respectively the properties of its elements).
The elements can be instances (e.g. UML classes), as well as relations between instances.
The recommendation generator represents the element generation of the theoretical pipeline.
Its results are stored in the :doc:`recommendation state <data/recommendationData>` of the :doc:`data structure <datastructures>`.

.. seealso:: For more information read the documentation about :doc:`the recommendation generator <stages/recommendationGeneration>`.


Connection Generation
----------------------------------
The connection generation connects the recommended entities to the entities of the given model.
Thus, it creates trace links between the input text and model.
The links are stored in the :doc:`connection state <data/connectionData>`.

.. seealso:: For more information read the documentation about :doc:`the connection generator <stages/connectionGeneration>`.

Inconsistency Generation
----------------------------------
The inconsistency generation is the last stage of the pipeline.
It makes a comparison between the found trace links and the entities of the model.
Thereby, it recognizes inconsistencies caused by missing mentions of model entities.


.. seealso:: For more information read the documentation about the :doc:`inconsistency generator <stages/inconsistencyGeneration>`.



.. toctree::
   :hidden:

   stages/textPreprocessing
   stages/textExtraction
   stages/modelExtraction
   stages/recommendationGeneration
   stages/connectionGeneration
   stages/inconsistencyGeneration