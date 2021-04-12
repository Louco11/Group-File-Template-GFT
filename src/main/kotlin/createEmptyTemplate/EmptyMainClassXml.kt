package createEmptyTemplate

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import constant.Constants

@JacksonXmlRootElement(localName = Constants.TagXml.HEAD_TAG)
class EmptyMainClassXml (
    @field:JacksonXmlElementWrapper(localName = Constants.TagXml.FIELD_NAME)
    var name: String = "No Name",
    @field:JacksonXmlProperty(localName = Constants.TagXml.FIELD_DESCRIPTION)
    var description: String = "Empty Template Description",
    @field:JacksonXmlProperty(localName = Constants.TagXml.FIELD_PATH)
    var path: String = ""
)
