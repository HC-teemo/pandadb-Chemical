package org.grapheco.pandadb.chem

import org.grapheco.lynx.LynxException
import org.grapheco.lynx.types.{InvalidValueException, LynxType, LynxValue, TypeCompareException}
import org.grapheco.pandadb.plugin.typesystem.AnyType

class Atom(val atom: String) extends AnyType {

  override def value: Any = this

  override def lynxType: LynxType = AtomType.instance

  override def serialize(): Array[Byte] = Array(atom.toByte)

  override def toString: String = atom.toString

  override def equals(obj: Any): Boolean = obj match {
    case o:Atom => atom.equals(o.atom)
  }
}

object Atom{
  private val periodicTableSymbols: Array[String] = Array(
    "H", "He", "Li", "Be", "B", "C", "N", "O", "F", "Ne",
    "Na", "Mg", "Al", "Si", "P", "S", "Cl", "Ar", "K", "Ca",
    "Sc", "Ti", "V", "Cr", "Mn", "Fe", "Co", "Ni", "Cu", "Zn",
    "Ga", "Ge", "As", "Se", "Br", "Kr", "Rb", "Sr", "Y", "Zr",
    "Nb", "Mo", "Tc", "Ru", "Rh", "Pd", "Ag", "Cd", "In", "Sn",
    "Sb", "Te", "I", "Xe", "Cs", "Ba", "La", "Ce", "Pr", "Nd",
    "Pm", "Sm", "Eu", "Gd", "Tb", "Dy", "Ho", "Er", "Tm", "Yb",
    "Lu", "Hf", "Ta", "W", "Re", "Os", "Ir", "Pt", "Au", "Hg",
    "Tl", "Pb", "Bi", "Po", "At", "Rn", "Fr", "Ra", "Ac", "Th",
    "Pa", "U", "Np", "Pu", "Am", "Cm", "Bk", "Cf", "Es", "Fm",
    "Md", "No", "Lr", "Rf", "Db", "Sg", "Bh", "Hs", "Mt", "Ds",
    "Rg", "Cn", "Nh", "Fl", "Mc", "Lv", "Ts", "Og"
  )

  private val allAtoms = periodicTableSymbols.map(s=> s->new Atom(s)).toMap

  def apply(atom: String): Atom = {
    println(atom)
    allAtoms.getOrElse(atom, throw InvalidValueException(atom))
  }
}

