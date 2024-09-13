package org.koin.test.verify

import org.koin.core.module.Module
import kotlin.reflect.KClass
import kotlin.time.measureTime

/**
 * Make a static verification about all declared classes constructors, to ensure they are all bound to an existing definition
 * Throws MissingDefinitionException if any definition is missing
 *
 * @param extraTypes - allow to declare extra type, to be bound above the existing definitions
 * @param injections - Experimental - defines parameters injection types to allow (normally used in parametersOf)
 * @throws MissingKoinDefinitionException
 */
fun Module.verify(extraTypes: List<KClass<*>> = listOf(), injections: List<ParameterTypeInjection>? = emptyList()) = Verify.verify(this, extraTypes,injections)

/**
 * Verify a list of Modules
 *
 * @see Module.verify
 */
fun List<Module>.verifyAll(extraTypes: List<KClass<*>> = listOf(), injections: List<ParameterTypeInjection>? = emptyList()) {
    forEach { module -> module.verify(extraTypes,injections) }
}

/**
 * Verify API
 *
 * Help to check current factory of a Module
 */
object Verify {

    internal val primitiveTypes = listOf(
        String::class,
        Int::class,
        Long::class,
        Double::class,
    )

    internal val whiteList = arrayListOf<KClass<*>>().apply {
        addAll(primitiveTypes)
    }

    fun addExtraTypes(vararg kClass: KClass<*>) {
        whiteList.addAll(kClass)
    }

    fun addExtraTypes(kClassList: List<KClass<*>>) {
        whiteList.addAll(kClassList)
    }

    /**
     * Make a static verification about all declared classes constructors, to ensure they are all bound to an existing definition
     * Throws MissingDefinitionException if any definition is missing
     *
     * @param module the moduel to verify
     * @param extraTypes allow to declare extra type, to be bound above the existing definitions
     * @throws MissingKoinDefinitionException
     */
    fun verify(module: Module, extraTypes: List<KClass<*>> = listOf(), injections: List<ParameterTypeInjection>? = null) {

        val verification = Verification(module, extraTypes, injections)
        println("Verifying module '$module' ...")

        val time = measureTime {
            verification.verify()
        }

        println("\n[SUCCESS] module '$this' has been verified in $time.")
    }
}
