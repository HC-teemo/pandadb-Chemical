package org.grapheco.pandadb.chem

import org.grapheco.lynx.types.LynxType
import org.grapheco.pandadb.plugin.typesystem.TypeFactory

class AtomFactory extends TypeFactory[Atom]{

  override def getType: LynxType = AtomType.instance

  override def fromBytes(bytes: Array[Byte]): Atom = Atom(new String(bytes))

  override def fromString(s: String): Atom = Atom(s)
}

class MoleculeFactory extends TypeFactory[Molecule]{
  override def getType: LynxType = MoleculeType.instance

  override def fromBytes(bytes: Array[Byte]): Molecule = new Molecule(new String(bytes))

  override def fromString(s: String): Molecule = new Molecule(s)
}
