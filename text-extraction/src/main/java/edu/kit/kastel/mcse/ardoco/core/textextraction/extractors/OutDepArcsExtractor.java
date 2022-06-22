/* Licensed under MIT 2021-2022. */
package edu.kit.kastel.mcse.ardoco.core.textextraction.extractors;

import java.util.Map;

import edu.kit.kastel.informalin.data.DataRepository;
import edu.kit.kastel.informalin.framework.configuration.Configurable;
import edu.kit.kastel.mcse.ardoco.core.api.agent.AbstractExtractor;
import edu.kit.kastel.mcse.ardoco.core.api.data.text.DependencyTag;
import edu.kit.kastel.mcse.ardoco.core.api.data.text.IWord;
import edu.kit.kastel.mcse.ardoco.core.api.data.textextraction.ITextState;
import edu.kit.kastel.mcse.ardoco.core.api.data.textextraction.MappingKind;
import edu.kit.kastel.mcse.ardoco.core.common.util.DataRepositoryHelper;
import edu.kit.kastel.mcse.ardoco.core.common.util.WordHelper;

/**
 * The analyzer examines the outgoing arcs of the current node.
 *
 * @author Sophie
 */

public class OutDepArcsExtractor extends AbstractExtractor {

    @Configurable
    private double nameOrTypeWeight = 0.5;

    @Configurable
    private double probability = 0.8;

    /**
     * Prototype constructor.
     */
    public OutDepArcsExtractor(DataRepository dataRepository) {
        super("OutDepArcsExtractor", dataRepository);
    }

    @Override
    public void run() {
        var textState = DataRepositoryHelper.getTextState(getDataRepository());
        for (var word : DataRepositoryHelper.getAnnotatedText(getDataRepository()).getWords()) {
            exec(textState, word);
        }
    }

    private void exec(ITextState textState, IWord word) {

        var nodeValue = word.getText();
        if (nodeValue.length() == 1 && !Character.isLetter(nodeValue.charAt(0))) {
            return;
        }
        examineOutgoingDepArcs(textState, word);
    }

    /**
     * Examines the outgoing dependencies of a node.
     */
    private void examineOutgoingDepArcs(ITextState textState, IWord word) {

        var outgoingDepArcs = WordHelper.getOutgoingDependencyTags(word);

        for (DependencyTag shortDepTag : outgoingDepArcs) {

            if (DependencyTag.AGENT == shortDepTag || DependencyTag.RCMOD == shortDepTag) {
                textState.addNounMapping(word, MappingKind.NAME, this, probability * nameOrTypeWeight);
                textState.addNounMapping(word, MappingKind.TYPE, this, probability * nameOrTypeWeight);
            } else if (DependencyTag.NUM == shortDepTag || DependencyTag.PREDET == shortDepTag) {
                textState.addNounMapping(word, MappingKind.TYPE, this, probability);
            }
        }
    }

    @Override
    protected void delegateApplyConfigurationToInternalObjects(Map<String, String> additionalConfiguration) {
        // emtpy
    }

}
