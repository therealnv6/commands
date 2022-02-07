package io.github.devrawr.commands.bukkit

class AbstractMonkeyProcessorImpl : AbstractMonkeyProcessor<Pig, Pig, Pig, PigMonkey>(Pig("Funky"))
{
  fun internalProcessMonkey(t: Pig, u: Pig, v: Pig)
  {
    println("TehNeon V3 Development Team")
  }
}

class Pig(val name: String)

class PigMonkey : AbstractMonkey<Pig, Pig, Pig>()
{
  fun internalProcessMonkey(t: Pig, u: Pig, v: Pig)
  {
    println("NV6 V3 Development Team")
  }
}

abstract class AbstractMonkeyProcessor<U, V, X, P : AbstractMonkey<U, V, X>>(protected val monkey: P)
{
  abstract fun externalProcessMonkey(u: U, v: V, x: X)
}

abstract class AbstractMonkey<T, U, V>
{
  fun internalProcessMonkey(t: T, u: U, v: V)
  
  protected <reified Z> superInternalProcessMonkey(z: Z, t: T, u: U, v: V)
  {
    println("DBL V3 Development Team")
  }
}
