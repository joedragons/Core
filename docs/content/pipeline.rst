Pipeline
===============

ArDoCo recognizes inconsistencies between a model and a documentation.
Therefore, it is divided into multiple steps that can be seen as pipeline:

.. image:: images/pipeline2.png
    :alt: center


The image shows the idea and processing of the approach.
Text and models are given as input.

If the text is not yet preprocessed, it is done via `INDIRECT <https://ps.ipd.kit.edu/176_indirect.php>`_.
The goal of the preprocessing is to analyse the text and annotate additional language information to it.
These could be dependencies, name-tags, or found relations between words.

Based on the given documentation ArDoCo first extracts potential entity names, entity types and relations from the text.
This stage is called :doc:`text extraction <stages/textExtraction>`.

After it, the :doc:`recommendation generation <stages/recommendationGeneration>` begins.
In this stage, the entity names and types are combined.
To increase the hit rate, we use the meta model as additional input for this phase.
Thereby, potential types are easier to detect.
The name-type combinations are traded as instances on the textual site.
Thereby, ArDoCo is able to recommend textual instances as potential trace links without knowledge of the instantiated model.

In the following :doc:`connection generation <stages/connectionGeneration>`, ArDoCo has access to the instantiated model and creates trace links between the recommended instances/ relations and the entities in the models.

The last stage is the :doc:`inconsistency generation <stages/inconsistencyGeneration>`.
In this stage, ArDoCo identifies entities without trace links as inconsistent states.


The respective `pipeline class <https://github.com/ArDoCo/Core/blob/main/pipeline/src/main/java/edu/kit/kastel/mcse/ardoco/core/pipeline/Pipeline.java>`_ is located in the `core module <https://github.com/ArDoCo/Core/>`_.

.. seealso:: For more details about the parts of the pipeline read the documentation on the :doc:`stages <stages>`.


Input
------------
The pipeline works with a single data structure for all steps: the :doc:`data structure <datastructures>`.
The *data structure* is filled with the information of the executed steps.
Then it is passed on through the pipeline.
Therefore, when the pipeline is instantiated, the prerequisites, a text in natural language and a readable model, have to be loaded into it.
The pipeline is executed via the method `runAndSave <https://github.com/ArDoCo/Core/blob/3b62cc78f0a9d4c60dc75796a401d83665f219f1/pipeline/src/main/java/edu/kit/kastel/mcse/ardoco/core/pipeline/Pipeline.java#L75>`_).

Text
^^^^^^^^^^
In a first step, the text should be given via a .txt file.
It should not contain any images, tables, or listings.
It is preprocessed via `INDIRECT <https://ps.ipd.kit.edu/176_indirect.php>`_.
The results of the preprocessing are stored as an annotated text.
This text is then used as base for the rest of the approach

If you want to enter a preprocessed text, it should be stored in `JSON <https://en.wikipedia.org/wiki/JSON#:~:text=JSON%20(JavaScript%20Object%20Notation%2C%20pronounced,(or%20other%20serializable%20values).>`_ format.
so that the `JsonTextProvider <https://github.com/ArDoCo/Core/blob/3b62cc78f0a9d4c60dc75796a401d83665f219f1/text-provider/src/main/java/edu/kit/kastel/mcse/ardoco/core/text/providers/json/JsonTextProvider.java#L52>`_ is able to extract the text and the annotations.
Notice, that some steps of the project depend on specific annotations, like pos-tags, dependencies, etc.!

.. seealso:: For more information read the documentation about :doc:`the textual data <data/textData>`.

Models
^^^^^^^^^^^^
ArDoCo can handle architectural and code models.
Models are read out using the suitable *model connector*.

In case of architectural models, this is the `PCM model connector <https://github.com/ArDoCo/Core/blob/3b62cc78f0a9d4c60dc75796a401d83665f219f1/model-provider/src/main/java/edu/kit/kastel/mcse/ardoco/core/model/pcm/PcmXMLModelConnector.java#L22>`_.
The connector requires a `PCM (Palladio Component Model) <https://www.palladio-simulator.com/science/palladio_component_model/>`_ in XML format.
Other connectors, for example, for other types of architectural models, can be defined analogously.

In case of code models, the `Java model connector <https://github.com/ArDoCo/Core/blob/3b62cc78f0a9d4c60dc75796a401d83665f219f1/model-provider/src/main/java/edu/kit/kastel/mcse/ardoco/core/model/java/JavaJsonModelConnector.java#L25>`_ extracts the classes and interfaces of the model.
Please note that we currently focus rather on instances than on relations.
Thereby, the extraction between the classes/ interfaces are currently not implemented.
Just like for architectural models, other code models can be used if additional connectors are provided.

The recognized instances and relations are written to the output directory if one was specified in the :doc:`CLI <quickstart/cli>`.

.. seealso:: For more information read the documentation about the :doc:`model connectors <stages/modelExtraction>` and :doc:`the model data <data/modelData>`.


