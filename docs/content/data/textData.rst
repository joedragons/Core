Text Data
================


The input text is read in via a `text connector <https://github.com/ArDoCo/Core/blob/main/text-provider/src/main/java/edu/kit/kastel/mcse/ardoco/core/text/providers/ITextConnector.java>`_.
Currently, we use preprocessing steps in style of Stanford Core NLP.
The `CoreNLPProvider <https://github.com/ArDoCo/Core/blob/main/text-provider/src/main/java/edu/kit/kastel/mcse/ardoco/core/text/providers/corenlp/CoreNLPProvider.java>`_ takes an InputStream as incoming text and applies selected coreference algorithms, annotators, and a dependency analysis to it.
The result is a `text <https://github.com/ArDoCo/Core/blob/main/common/src/main/java/edu/kit/kastel/mcse/ardoco/core/api/data/text/IText.java>`_ that is similar to an preprocessed/ annotated Text.
All further steps, relying on textual input, work with this annotated text.

A text consists of `sentences <https://github.com/ArDoCo/Core/blob/main/common/src/main/java/edu/kit/kastel/mcse/ardoco/core/api/data/text/ISentence.java>`_, that can be subdivided into `phrases <https://github.com/ArDoCo/Core/blob/main/common/src/main/java/edu/kit/kastel/mcse/ardoco/core/api/data/text/IPhrase.java>`_ and `words <https://github.com/ArDoCo/Core/blob/main/common/src/main/java/edu/kit/kastel/mcse/ardoco/core/api/data/text/IWord.java>`_.

The annotations of the text are accessed via the `core document <https://nlp.stanford.edu/nlp/javadoc/javanlp/edu/stanford/nlp/pipeline/CoreDocument.html>`_ of stanford core nlp.
While the text provides access to `coreferences <https://github.com/ArDoCo/Core/blob/main/common/src/main/java/edu/kit/kastel/mcse/ardoco/core/api/data/text/ICorefCluster.java>`_, phrases have a `phrase type <https://github.com/ArDoCo/Core/blob/main/common/src/main/java/edu/kit/kastel/mcse/ardoco/core/api/data/text/PhraseType.java>`_, and words `part of speech tags <https://github.com/ArDoCo/Core/blob/main/common/src/main/java/edu/kit/kastel/mcse/ardoco/core/api/data/text/POSTag.java>`_ as well as `dependency tags <https://github.com/ArDoCo/Core/blob/main/common/src/main/java/edu/kit/kastel/mcse/ardoco/core/api/data/text/DependencyTag.java>`_.
The latter are accessed via the `core labels <https://nlp.stanford.edu/nlp/javadoc/javanlp/edu/stanford/nlp/ling/CoreLabel.html>`_ of stanford.

.. uml::

    @startuml

    interface IText{
        getFirstWord() : IWord
        getLength() : int
        getWords() : Words[]
        getCorefClusters() : ICorefCluster[]
        getSentences() : ISentence[]
    }


    interface IPhrase{
        getSentenceNo() : int
        getSentence() : ISentence
        getText() : String
        getPhraseType() : PhraseType
        getContainedWords() : IWord[]
        isSubPhraseOf( IPhrase other) : boolean
        isSuperPhraseOf( IPhrase other) : boolean
    }

    interface ISentence{
        getSentenceNumber() : int
        getWords() : IWord[]
        getText() : String
        getPhrases() : IPhrase[]
    }

    interface IWord{
        getSentenceNo() : int
        getSentence() : ISentence
        getText() : String
        getPosTag() : POSTag
        getPreWord() : IWord
        getNextWord() : IWord
        getPosition() : int
        getLemma() : String
        getOutgoingDependencyWordsWithType( DependencyTag dependencyTag )
        getIncomingDependencyWordsWithType( DependencyTag dependencyTag )
    }

    enum DependencyTag{}

    enum POSTag {}

    enum PhraseType{}

    interface ITextConnector{
        getAnnotatedText() : IText
        getAnnotatedText( String textName ) : IText
    }

    class CoreNLPProvider{
        {static} COREF_ALGORITHMS : String[]
        {static} ANNOTATORS : String
        {static} DEPENDENCIES_ANNOTATION : String
        - text : InputStream
        - annotatedText : IText
    }

    class Text{
        - coreDocument : CoreDocument
        - sentences : ISentence[]
        - words : IWord[]
    }

    class Word{
        - token : CoreLabel
        - coreDocument : CoreDocument
        - index : int
        - preWord : IWord
        - nextWord : IWord
    }

    ITextConnector <|.d. CoreNLPProvider
    CoreNLPProvider -r-> IText
    IText <|.d. Text
    Text -r-> ISentence
    Text -[hidden]d- IWord

    Text -d-> IWord
    ISentence .d.> IPhrase
    IPhrase .l.> IWord
    ISentence ..> IWord

    IWord <|.d. Word
    IWord ..> POSTag
    IWord ..> DependencyTag
    IPhrase ..> PhraseType


    @enduml






