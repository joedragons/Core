package edu.kit.ipd.consistency_analyzer.agents_extractors.agents;

public abstract class ResolverAgent extends Agent {

	/**
	 * Prototype Constructor
	 */
	protected ResolverAgent(Class<? extends Configuration> configType) {
		super(configType);
	}

	protected ResolverAgent(DependencyType dependencyType, Class<? extends Configuration> configType) {
		super(dependencyType, configType);
	}

}
