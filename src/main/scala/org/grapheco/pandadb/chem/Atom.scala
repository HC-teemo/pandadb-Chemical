package org.grapheco.pandadb.chem

import org.grapheco.lynx.types.LynxType
import org.grapheco.pandadb.plugin.typesystem.AnyType

class Atom(atom: Char) extends AnyType {

  override def value: Any = atom

  override def lynxType: LynxType = AtomType.instance

  override def serialize(): Array[Byte] = Array(atom.toByte)

  override def toString: String = atom.toString
}

