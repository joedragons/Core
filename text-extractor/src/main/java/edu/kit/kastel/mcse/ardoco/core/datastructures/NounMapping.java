package edu.kit.kastel.mcse.ardoco.core.datastructures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import edu.kit.kastel.mcse.ardoco.core.datastructures.common.SimilarityUtils;
import edu.kit.kastel.mcse.ardoco.core.datastructures.definitions.INounMapping;
import edu.kit.kastel.mcse.ardoco.core.datastructures.definitions.IWord;
import edu.kit.kastel.mcse.ardoco.core.datastructures.definitions.MappingKind;

/**
 * The Class NounMapping is a basic realization of {@link INounMapping}.
 */
public class NounMapping implements INounMapping {

    private List<IWord> words;
    private String reference;
    private List<String> occurrences;

    private MappingKind mostProbableKind;
    private Double highestProbability;
    private Map<MappingKind, Double> distribution;

    /**
     * Instantiates a new noun mapping.
     *
     * @param words        the words
     * @param distribution the distribution
     * @param reference    the reference
     * @param occurrences  the occurrences
     */
    public NounMapping(List<IWord> words, Map<MappingKind, Double> distribution, String reference, List<String> occurrences) {
        this.words = new ArrayList<>(words);
        initializeDistribution(distribution);
        this.reference = reference;
        this.occurrences = new ArrayList<>(occurrences);
        mostProbableKind = distribution.keySet().stream().max((p1, p2) -> distribution.get(p1).compareTo(distribution.get(p2))).orElse(null);
        highestProbability = mostProbableKind != null ? distribution.get(mostProbableKind) : 0.0;
    }

    private void initializeDistribution(Map<MappingKind, Double> distribution) {

        this.distribution = new EnumMap<>(distribution);

        if (!distribution.containsKey(MappingKind.NAME)) {
            this.distribution.put(MappingKind.NAME, 0.0);
        }
        if (!distribution.containsKey(MappingKind.TYPE)) {
            this.distribution.put(MappingKind.TYPE, 0.0);
        }
        if (!distribution.containsKey(MappingKind.NAME_OR_TYPE)) {
            this.distribution.put(MappingKind.NAME_OR_TYPE, 0.0);
        }

    }

    /**
     * Instantiates a new noun mapping.
     *
     * @param words       the words
     * @param kind        the kind
     * @param probability the probability
     * @param reference   the reference
     * @param occurrences the occurrences
     */
    public NounMapping(List<IWord> words, MappingKind kind, double probability, String reference, List<String> occurrences) {
        distribution = new EnumMap<>(MappingKind.class);
        distribution.put(kind, probability);

        this.words = new ArrayList<>(words);
        initializeDistribution(distribution);
        this.reference = reference;
        this.occurrences = new ArrayList<>(occurrences);
        mostProbableKind = distribution.keySet().stream().max((p1, p2) -> distribution.get(p1).compareTo(distribution.get(p2))).orElse(null);
        highestProbability = mostProbableKind != null ? distribution.get(mostProbableKind) : 0.0;
    }

    /**
     * Returns the occurrences of this mapping.
     *
     * @return all appearances of the mapping
     */
    @Override
    public final List<String> getOccurrences() {
        return new ArrayList<>(occurrences);
    }

    /**
     * Returns all nodes contained by the mapping
     *
     * @return all mapping nodes
     */
    @Override
    public final List<IWord> getWords() {
        return new ArrayList<>(words);
    }

    /**
     * Adds nodes to the mapping, if they are not already contained.
     *
     * @param nodes graph nodes to add to the mapping
     */
    @Override
    public final void addNodes(List<IWord> nodes) {
        for (IWord n : nodes) {
            addNode(n);
        }
    }

    /**
     * Adds a node to the mapping, it its not already contained.
     *
     * @param n graph node to add.
     */
    @Override
    public final void addNode(IWord n) {
        if (!words.contains(n)) {
            words.add(n);
        }
    }

    /**
     * Returns the reference, the comparable and naming attribute of this mapping.
     *
     * @return the reference
     */
    @Override
    public final String getReference() {
        return reference;
    }

    /**
     * Returns the sentence numbers of occurrences, sorted.
     *
     * @return sentence numbers of the occurrences of this mapping.
     */
    @Override
    public final List<Integer> getMappingSentenceNo() {
        List<Integer> positions = new ArrayList<>();
        for (IWord n : words) {
            positions.add(n.getSentenceNo() + 1);
        }
        Collections.sort(positions);
        return positions;
    }

    /**
     * Adds occurrences to the mapping
     *
     * @param newOccurances occurrences to add
     */
    @Override
    public final void addOccurrence(List<String> newOccurances) {
        for (String o : newOccurances) {
            if (!occurrences.contains(o)) {
                occurrences.add(o);
            }
        }
    }

    /**
     * Copies all nodes and occurrences matching the occurrence to another mapping
     *
     * @param occurrence     the occurrence to copy
     * @param createdMapping the other mapping
     */
    @Override
    public final void copyOccurrencesAndNodesTo(String occurrence, INounMapping createdMapping) {
        List<IWord> occNodes = words.stream().filter(n -> n.getText().equals(occurrence)).collect(Collectors.toList());
        createdMapping.addNodes(occNodes);
        createdMapping.addOccurrence(List.of(occurrence));
    }

    /**
     * Returns a list of all node lemmas encapsulated by a mapping.
     *
     * @return list of containing node lemmas
     */
    public final List<String> getMappingLemmas() {
        return words.stream().map(IWord::getLemma).collect(Collectors.toList());
    }

    /**
     * Adds the kind with probability.
     *
     * @param kind        the kind
     * @param probability the probability
     */
    public void addKindWithProbability(MappingKind kind, double probability) {
        recalculateProbability(kind, probability);
    }

    @Override
    public NounMapping createCopy() {
        return new NounMapping(words, distribution, reference, occurrences);
    }

    @Override
    public Map<MappingKind, Double> getDistribution() {
        return new EnumMap<>(distribution);
    }

    /**
     * Splits all occurrences with a whitespace in it at their spaces and returns all parts that are similar to the
     * reference. If it contains a separator or similar to the reference it is added to the comparables as a whole.
     *
     * @return all parts of occurrences (splitted at their spaces) that are similar to the reference.
     */
    @Override
    public List<String> getRepresentativeComparables() {
        List<String> comparables = new ArrayList<>();
        for (String occ : occurrences) {
            if (SimilarityUtils.containsSeparator(occ)) {
                List<String> parts = SimilarityUtils.splitAtSeparators(occ);
                for (String part : parts) {
                    if (SimilarityUtils.areWordsSimilar(reference, part)) {
                        comparables.add(part);
                    }
                }
                comparables.add(occ);
            } else if (SimilarityUtils.areWordsSimilar(reference, occ)) {
                comparables.add(occ);
            }
        }
        return comparables;
    }

    /**
     * Sets the probability of the mapping
     *
     * @param newProbability probability to set on
     */
    @Override
    public void hardSetProbability(double newProbability) {
        recalculateProbability(mostProbableKind, newProbability);
    }

    /**
     * Returns the probability of being a mapping of its kind.
     *
     * @return probability of being a mapping of its kind.
     */
    @Override
    public double getProbability() {
        return highestProbability;
    }

    /**
     * Returns the kind: name, type, name_or_type.
     *
     * @return the kind
     */
    @Override
    public MappingKind getKind() {
        return mostProbableKind;
    }

    /**
     *
     * @param kind        the new kind
     * @param probability the probability of the new mappingType
     */
    @Override
    public void changeMappingType(MappingKind kind, double probability) {
        recalculateProbability(kind, highestProbability * probability);
    }

    private void recalculateProbability(MappingKind kind, double newProbability) {

        double currentProbability = distribution.get(kind);
        distribution.put(kind, currentProbability + newProbability);

        mostProbableKind = distribution.keySet().stream().max((p1, p2) -> distribution.get(p1).compareTo(distribution.get(p2))).orElse(null);
        if (mostProbableKind != null) {
            if (mostProbableKind == MappingKind.NAME_OR_TYPE && (distribution.get(MappingKind.NAME) > 0 || distribution.get(MappingKind.TYPE) > 0)) {

                if (distribution.get(MappingKind.NAME) >= distribution.get(MappingKind.TYPE)) {
                    mostProbableKind = MappingKind.NAME;
                } else {
                    mostProbableKind = MappingKind.TYPE;
                }
            }
            highestProbability = distribution.get(mostProbableKind);
        }
    }

    /**
     * Updates the reference if the probability is high enough.
     *
     * @param ref         new reference
     * @param probability probability for the new reference.
     */
    @Override
    public void updateReference(String ref, double probability) {
        if (probability > highestProbability * 4) {
            reference = ref;
        }
    }

    @Override
    public String toString() {
        return "NounMapping [" + "distribution="
                + distribution.entrySet().stream().map(entry -> entry.getKey() + ":" + entry.getValue()).collect(Collectors.joining(",")) + //
                ", reference=" + reference + //
                ", node=" + String.join(", ", occurrences) + //
                ", position=" + String.join(", ", words.stream().map(word -> String.valueOf(word.getPosition())).collect(Collectors.toList())) + //
                ", probability=" + highestProbability + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(reference);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        NounMapping other = (NounMapping) obj;
        return Objects.equals(reference, other.reference);
    }

    /**
     * Updates the probability
     *
     * @param newProbability the probability to update with.
     */
    @Override
    public void updateProbability(double newProbability) {
        if (highestProbability == 1.0) {
            return;
        }

        if (newProbability == 1.0) {
            highestProbability = newProbability;
            distribution.put(mostProbableKind, newProbability);
        } else if (highestProbability >= newProbability) {
            double porbabilityToSet = highestProbability + newProbability * (1 - highestProbability);
            recalculateProbability(mostProbableKind, porbabilityToSet);
        } else {
            double porbabilityToSet = (highestProbability + newProbability) * 0.5;
            recalculateProbability(mostProbableKind, porbabilityToSet);
        }
    }

    @Override
    public double getProbabilityForName() {
        return distribution.get(MappingKind.NAME);
    }

    @Override
    public double getProbabilityForType() {
        return distribution.get(MappingKind.TYPE);
    }

    @Override
    public double getProbabilityForNort() {
        return distribution.get(MappingKind.NAME_OR_TYPE);
    }

}
