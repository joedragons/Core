Data Structures
=================

The stages communicate via a blackboard mechanism.
Every stage reads and writes to the `DataStructure <https://github.com/ArDoCo/Core/blob/main/common/src/main/java/edu/kit/kastel/mcse/ardoco/core/api/data/DataStructure.java>`_.

The DataStructure encapsulates states of different kind:

.. list-table::
    :widths: 25 75

    * - :doc:`Model State <data/modelData>`
      - encapsulates the input model
    * - :doc:`Text Data <data/textData>`
      - contains the input text and annotations from the preprocessing
    * - :doc:`Text Extraction State <data/textextractionData>`
      - contains potential names, types and relations of elements found in the text
    * - :doc:`Recommendation State <data/recommendationData>`
      - contains recommendations for potential element and relations from the text to the model
    * - :doc:`Connection State <data/connectionData>`
      - contains trace links between recommended elements/ relations and model elements/ relations
    * - :doc:`Inconsistency State <data/inconsistencyData>`
      - contains identified inconsistencies between text and model

To set up the `DataStructure <https://github.com/ArDoCo/Core/blob/main/common/src/main/java/edu/kit/kastel/mcse/ardoco/core/api/data/DataStructure.java>`_, a Text State and a Model State are needed.
However, as described in :doc:`pipeline`, ArDoCo can work with architecture and code models.
Thereby, the DataStructure can have multiple Model States that are differentiated via their modelId.
Since the Text Extraction State is directly created from the text without any influence of the model or metamodel there should always be only one of its kind.
In contrast to it, the other states use model information.
Thereby, they have also to be used with respect to the used model and are also accessed via the modelId of the used model.


.. toctree::
   :hidden:

   data/textData
   data/textextractionData
   data/modelData
   data/recommendationData
   data/connectionData
   data/inconsistencyData


