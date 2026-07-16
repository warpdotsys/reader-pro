package io.legado.app.data.entities

import io.legado.app.model.analyzeRule.RuleDataInterface

interface BaseBook : RuleDataInterface {
    var name: String
    var author: String
}
