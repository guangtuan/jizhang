package tech.igrant.jizhang.account

enum class AccountType {
    ASSETS {
        override fun toString(): String {
            return "ASSETS"
        }
    },
    LIABILITIES {
        override fun toString(): String {
            return "LIABILITIES"
        }
    }
}