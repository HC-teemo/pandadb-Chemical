package org.grapheco.pandadb.chem

import org.grapheco.lynx.types.{LTAny, LynxType}

object AtomType{
  val instance: AtomType = new AtomType {
    override def parentType: LynxType = LTAny

    override def toString: String = "Atom"
  }
}

object MoleculeType{
  val instance: MoleculeType = new MoleculeType {
    override def parentType: LynxType = LTAny

    override def toString: String = "Molecule"
  }
}

sealed abstract class MoleculeType extends LynxType
sealed abstract class AtomType extends LynxType

