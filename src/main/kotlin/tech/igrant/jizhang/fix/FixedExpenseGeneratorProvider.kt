package tech.igrant.jizhang.fix

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component
import tech.igrant.jizhang.detail.Detail
import javax.annotation.PostConstruct

@Component
class FixedExpenseGeneratorProvider : ApplicationContextAware {

    private lateinit var applicationContext: ApplicationContext

    private val generators: MutableList<FixedExpenseGenerator> = mutableListOf()

    @PostConstruct
    fun init() {
        val beans = applicationContext.getBeansOfType(FixedExpenseGenerator::class.java)
        generators.addAll(beans.values)
    }

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.applicationContext = applicationContext
    }

    fun apply(fixedExpenses: FixedExpenses): List<Detail> {
        return generators.firstOrNull { canGenerate(it, fixedExpenses) }
                ?.apply(fixedExpenses)
                ?: throw UnsupportedOperationException("current can not handle fixedExpense with form: ${fixedExpenses.fixedForm}")
    }

    private fun canGenerate(it: FixedExpenseGenerator, fixedExpenses: FixedExpenses) =
            it.javaClass.getAnnotation(CanHandleFixedForm::class.java)?.form == fixedExpenses.fixedForm

}