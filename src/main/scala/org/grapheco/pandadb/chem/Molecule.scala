package org.grapheco.pandadb.chem

import org.grapheco.lynx.types.composite.LynxList
import org.grapheco.lynx.types.property.LynxNull
import org.grapheco.lynx.types.structural.LynxPropertyKey
import org.grapheco.lynx.types.traits.{HasProperty, LynxComputable}
import org.grapheco.lynx.types.{LTAny, LynxType, LynxValue}
import org.grapheco.pandadb.plugin.typesystem.AnyType

class Molecule(val m: String) extends AnyType with HasProperty with LynxComputable{

  override def value: Any = this

  override def toString: String = s"<$m>"

  override def lynxType: LynxType = MoleculeType.instance

  override def serialize(): Array[Byte] = m.getBytes

  /*
  Computable
   */
  override def add(another: LynxValue): LynxValue = another match {
    case m:Molecule => reaction(this, m)
    case _ => LynxNull
  }

  override def subtract(another: LynxValue): LynxValue = LynxNull

  override def multiply(another: LynxValue): LynxValue = LynxNull

  override def divide(another: LynxValue): LynxValue = LynxNull

  private def reaction(molecule: Molecule, molecule2: Molecule): Molecule = (molecule.m, molecule2.m) match {
    case ("HH", "OO") => new Molecule("H2O")
    case ("Na", "HCl") => new Molecule("NaCl")
    case _ => new Molecule("")
  }

  override def equals(obj: Any): Boolean = obj match {
    case o:Molecule => o.m.equals(m)
  }

  /*
  Sub-property
   */
  override def keys: Seq[LynxPropertyKey] = Seq(LynxPropertyKey("atoms"))

  override def property(propertyKey: LynxPropertyKey): Option[LynxValue] = propertyKey.value match {
    case "atoms" => Some(LynxList(atoms()))
  }

  def atoms(): List[Atom] = {
    val elementPattern = "([A-Z][a-z]?)".r
    val elements = elementPattern.findAllIn(m).toList
    elements.map(Atom.apply)
  }
}




