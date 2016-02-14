package org.zwobble.mammoth;

import org.zwobble.mammoth.docx.*;
import org.zwobble.mammoth.html.Html;
import org.zwobble.mammoth.results.Result;
import org.zwobble.mammoth.xml.XmlElement;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipFile;

public class Mammoth {
    public static Result<String> convertToHtml(File file) {
        try (DocxFile zipFile = new ZippedDocxFile(new ZipFile(file))) {
            XmlElement documentXml = OfficeXml.parseXml(zipFile.getInputStream("word/document.xml"));

            Styles styles = Styles.EMPTY;
            Numbering numbering = Numbering.EMPTY;
            Relationships relationships = Relationships.EMPTY;
            ContentTypes contentTypes = ContentTypes.DEFAULT;
            FileReader fileReader = new FileReader() {
                @Override
                public InputStream getInputStream(String uri) throws IOException {
                    throw new UnsupportedOperationException();
                }
            };
            DocumentXmlReader reader = new DocumentXmlReader(new BodyXmlReader(
                styles,
                numbering,
                relationships,
                contentTypes,
                zipFile,
                fileReader));
            // TODO: prefix
            String idPrefix = "document";
            return reader.readElement(documentXml)
                .map(nodes -> new DocumentConverter(idPrefix).convertToHtml(nodes))
                .map(Html::write);
        } catch (IOException e) {
            throw new UnsupportedOperationException("Should return a result of failure");   
        }
    }
}
