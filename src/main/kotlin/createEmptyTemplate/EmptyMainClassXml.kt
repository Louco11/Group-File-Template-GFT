package createEmptyTemplate

import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "template")
data class EmptyMainClassXml(
    @XmlAttribute val name: String,
    @XmlAttribute val description: String
)