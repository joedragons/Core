/* Licensed under MIT 2022. */
package edu.kit.kastel.mcse.ardoco.core.textextraction;

import java.util.function.Function;

import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;

import edu.kit.kastel.mcse.ardoco.core.api.agent.Claimant;
import edu.kit.kastel.mcse.ardoco.core.api.data.text.Word;
import edu.kit.kastel.mcse.ardoco.core.api.data.textextraction.MappingKind;
import edu.kit.kastel.mcse.ardoco.core.api.data.textextraction.NounMapping;
import edu.kit.kastel.mcse.ardoco.core.common.util.ElementWrapper;

public interface TextStateStrategy {

    NounMapping addOrExtendNounMapping(Word word, MappingKind kind, Claimant claimant, double probability, ImmutableList<String> surfaceForms);

    ElementWrapper<NounMapping> wrap(NounMapping nounMapping);

    NounMapping mergeNounMappings(NounMapping nounMapping, NounMapping nounMapping2, ImmutableList<Word> referenceWords, String reference,
            MappingKind mappingKind, Claimant claimant, double probability);

    NounMapping mergeNounMappings(NounMapping nounMapping, MutableList<NounMapping> nounMappingsToMerge, Claimant claimant);

    Function<TextStateImpl, TextStateStrategy> creator();
}
