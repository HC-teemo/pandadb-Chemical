package org.grapheco.pandadb.chem

import org.grapheco.pandadb.plugin.typesystem.ExtensionTypePlugin

class ChemicalPlugin extends ExtensionTypePlugin {
  override def registerAll(): Unit = {
    registerFactory(new AtomFactory, new MoleculeFactory)
    registerType(AtomType.instance, classOf[Atom])
    registerType(MoleculeType.instance, classOf[Molecule])
    registerFunction(classOf[ChemicalFunctions])
  }

  override def getName: String = "Chemical Plugin"
}
