package tech.igrant.jizhang.account

import javax.persistence.*

@Entity
@Table(name = "account")
class Account(
        var type: String,
        var user: String,
        var name: String,
        var description: String,
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null
)