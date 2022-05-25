
Model
==========

The input model is extracted via a `Model Connector <https://github.com/ArDoCo/Core/tree/main/model-provider/src/main/java/edu/kit/kastel/mcse/ardoco/core/model>`_.
The interface requires an id of the model, a metamodel and the instances.

Currently, we provide two model connectors: a `Json connector for code models <https://github.com/ArDoCo/Core/blob/main/model-provider/src/main/java/edu/kit/kastel/mcse/ardoco/core/model/JavaJsonModelConnector.java>`_ and an `XML connector for PCM models <https://github.com/ArDoCo/Core/blob/main/model-provider/src/main/java/edu/kit/kastel/mcse/ardoco/core/model/PcmXMLModelConnector.java>`_.
The connectors read in the respective model and write them in the unified model state.
For more information on the connectors go to the :doc:`model extraction state <../stages/modelExtraction>`.


Each read in model is stored in a `model state <https://github.com/ArDoCo/Core/blob/main/common/src/main/java/edu/kit/kastel/mcse/ardoco/core/api/data/model/IModelState.java>`_.
A model has a model id to access it.
To differentiate between architectural and code models, the ``metamodel`` enumeration has to be set to the respective element.

Currently, we use the `model extraction state <https://github.com/ArDoCo/Core/blob/main/model-provider/src/main/java/edu/kit/kastel/mcse/ardoco/core/model/ModelExtractionState.java>`_ as implementation of the model state.
The state encapsulates the instances in `model instances <https://github.com/ArDoCo/Core/blob/c5ab3aaa5071de224889ca2491cc8390877b136c/common/src/main/java/edu/kit/kastel/mcse/ardoco/core/api/data/model/IModelInstance.java>`_ and provides easy access to the types and names of all instances.

A `model instance <https://github.com/ArDoCo/Core/blob/c5ab3aaa5071de224889ca2491cc8390877b136c/common/src/main/java/edu/kit/kastel/mcse/ardoco/core/api/data/model/IModelInstance.java>`_ has a ``full name``, that is the original name of the instance.
Analogously a ``full type`` is defined.
Moreover, getter for partial identifiers are provided to offer a unified form for the following steps.
Finally, every instance has a ``uid``.

.. uml::

    @startuml

    class ModelExtractionState{
    }

    interface IModelState{
        getModelId() : String
        getMetamodel() : Metamodel
        getInstancesOfType(String type) : IModelInstance[]
        getInstanceTypes() : String[]
        getNames() : String[]
        getInstances() : IModelInstances[]
    }

    enum Metamodel{
        CODE
        ARCHITECTURE
    }

    interface IModelInstance{
        getFullName() : String
        getFullType() : String
        getNameParts() : String[]
        getTypeParts() : String[]
        getUid() : String
    }

    class Instance{
    }

    class ModelProvider{
        execute(... additionalSettings) : IModelState
    }

    interface IModelConnector{
        getModelId() : String
        getMetamodel() : Metamodel
        getInstances() : IModelInstance[]
    }

    class JavaJsonModelConnector {
        - javaProject : JavaProject
    }

    class PcmXMLModelConnector{
        - repository : PCMRepository
    }

    ModelExtractionState -l-> Metamodel
    IModelState <|.d. ModelExtractionState
    ModelExtractionState -r-> IModelInstance
    Instance .u.|> IModelInstance
    IModelConnector <-l- ModelProvider
    IModelConnector <|.d. PcmXMLModelConnector
    IModelConnector <|.d. JavaJsonModelConnector
    ModelProvider .d.> IModelState

    @enduml



