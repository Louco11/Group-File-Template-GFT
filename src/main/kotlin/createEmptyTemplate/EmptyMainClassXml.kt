package createEmptyTemplate

import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "template")
class EmptyMainClassXml {
    private var name: String = ""
    private var description: String = ""

    @JvmName("setName1")
    @XmlAttribute
    fun setName(name: String) {
        this.name = name
    }

    @JvmName("setDescription1")
    @XmlAttribute
    fun setDescription(name: String) {
        this.description = name
    }
}