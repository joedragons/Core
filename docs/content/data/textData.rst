Text Data
================

.. note:: This documentation is currently built up



.. uml::

    @startuml

    interface IText{
        getFirstWord() : IWord
        getLength() : int
        getWords() : Words[]
        getCorefClusters() : ICorefCluster[]
        getSentences() : ISentence[]
    }

    interface ICorefCluster{
        id() : int
        mentions() : IWord[[]]
        {static} getTextForMention( IWord[] mention) : String
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


    IText .r.> ICorefCluster
    @enduml

Word
____________

Sentence
______________

PosTags
_____________

Dependencies
_______________

Coreferences
_________________










