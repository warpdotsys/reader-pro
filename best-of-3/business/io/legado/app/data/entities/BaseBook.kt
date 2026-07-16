/** Business rewrite from reader-pro-3.2.14.jar — phase10. */

package io.legado.app.data.entities

import io.legado.app.model.analyzeRule.RuleDataInterface

/**
 * 书籍规则数据接口（AnalyzeRule.book / putVariable）。
 */
interface BaseBook : RuleDataInterface {
    var name: String
    var author: String
}
