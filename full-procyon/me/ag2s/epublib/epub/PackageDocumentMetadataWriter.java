// 
// Decompiled by Procyon v0.6.0
// 

package me.ag2s.epublib.epub;

import me.ag2s.epublib.domain.Identifier;
import java.util.List;
import java.io.IOException;
import java.util.Iterator;
import javax.xml.namespace.QName;
import java.util.Map;
import me.ag2s.epublib.util.StringUtil;
import me.ag2s.epublib.domain.Date;
import me.ag2s.epublib.domain.Author;
import org.xmlpull.v1.XmlSerializer;
import me.ag2s.epublib.domain.EpubBook;

public class PackageDocumentMetadataWriter extends PackageDocumentBase
{
    public static void writeMetaData(final EpubBook book, final XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
        serializer.startTag("http://www.idpf.org/2007/opf", "metadata");
        serializer.setPrefix("dc", "http://purl.org/dc/elements/1.1/");
        serializer.setPrefix("", "http://www.idpf.org/2007/opf");
        writeIdentifiers(book.getMetadata().getIdentifiers(), serializer);
        writeSimpleMetdataElements("title", book.getMetadata().getTitles(), serializer);
        writeSimpleMetdataElements("subject", book.getMetadata().getSubjects(), serializer);
        writeSimpleMetdataElements("description", book.getMetadata().getDescriptions(), serializer);
        writeSimpleMetdataElements("publisher", book.getMetadata().getPublishers(), serializer);
        writeSimpleMetdataElements("type", book.getMetadata().getTypes(), serializer);
        writeSimpleMetdataElements("rights", book.getMetadata().getRights(), serializer);
        for (final Author author : book.getMetadata().getAuthors()) {
            serializer.startTag("http://purl.org/dc/elements/1.1/", "creator");
            serializer.attribute("http://www.idpf.org/2007/opf", "role", author.getRelator().getCode());
            serializer.attribute("http://www.idpf.org/2007/opf", "file-as", author.getLastname() + ", " + author.getFirstname());
            serializer.text(author.getFirstname() + " " + author.getLastname());
            serializer.endTag("http://purl.org/dc/elements/1.1/", "creator");
        }
        for (final Author author : book.getMetadata().getContributors()) {
            serializer.startTag("http://purl.org/dc/elements/1.1/", "contributor");
            serializer.attribute("http://www.idpf.org/2007/opf", "role", author.getRelator().getCode());
            serializer.attribute("http://www.idpf.org/2007/opf", "file-as", author.getLastname() + ", " + author.getFirstname());
            serializer.text(author.getFirstname() + " " + author.getLastname());
            serializer.endTag("http://purl.org/dc/elements/1.1/", "contributor");
        }
        for (final Date date : book.getMetadata().getDates()) {
            serializer.startTag("http://purl.org/dc/elements/1.1/", "date");
            if (date.getEvent() != null) {
                serializer.attribute("http://www.idpf.org/2007/opf", "event", date.getEvent().toString());
            }
            serializer.text(date.getValue());
            serializer.endTag("http://purl.org/dc/elements/1.1/", "date");
        }
        if (StringUtil.isNotBlank(book.getMetadata().getLanguage())) {
            serializer.startTag("http://purl.org/dc/elements/1.1/", "language");
            serializer.text(book.getMetadata().getLanguage());
            serializer.endTag("http://purl.org/dc/elements/1.1/", "language");
        }
        if (book.getMetadata().getOtherProperties() != null) {
            for (final Map.Entry<QName, String> mapEntry : book.getMetadata().getOtherProperties().entrySet()) {
                serializer.startTag(mapEntry.getKey().getNamespaceURI(), "meta");
                serializer.attribute("", "property", mapEntry.getKey().getLocalPart());
                serializer.text((String)mapEntry.getValue());
                serializer.endTag(mapEntry.getKey().getNamespaceURI(), "meta");
            }
        }
        if (book.getCoverImage() != null) {
            serializer.startTag("http://www.idpf.org/2007/opf", "meta");
            serializer.attribute("", "name", "cover");
            serializer.attribute("", "content", book.getCoverImage().getId());
            serializer.endTag("http://www.idpf.org/2007/opf", "meta");
        }
        serializer.startTag("http://www.idpf.org/2007/opf", "meta");
        serializer.attribute("", "name", "generator");
        serializer.attribute("", "content", "Ag2S EpubLib");
        serializer.endTag("http://www.idpf.org/2007/opf", "meta");
        serializer.startTag("http://www.idpf.org/2007/opf", "meta");
        serializer.attribute("", "name", "duokan-body-font");
        serializer.attribute("", "content", "DK-SONGTI");
        serializer.endTag("http://www.idpf.org/2007/opf", "meta");
        serializer.endTag("http://www.idpf.org/2007/opf", "metadata");
    }
    
    private static void writeSimpleMetdataElements(final String tagName, final List<String> values, final XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
        for (final String value : values) {
            if (StringUtil.isBlank(value)) {
                continue;
            }
            serializer.startTag("http://purl.org/dc/elements/1.1/", tagName);
            serializer.text(value);
            serializer.endTag("http://purl.org/dc/elements/1.1/", tagName);
        }
    }
    
    private static void writeIdentifiers(final List<Identifier> identifiers, final XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
        final Identifier bookIdIdentifier = Identifier.getBookIdIdentifier(identifiers);
        if (bookIdIdentifier == null) {
            return;
        }
        serializer.startTag("http://purl.org/dc/elements/1.1/", "identifier");
        serializer.attribute("", "id", "duokan-book-id");
        serializer.attribute("http://www.idpf.org/2007/opf", "scheme", bookIdIdentifier.getScheme());
        serializer.text(bookIdIdentifier.getValue());
        serializer.endTag("http://purl.org/dc/elements/1.1/", "identifier");
        for (final Identifier identifier : identifiers.subList(1, identifiers.size())) {
            if (identifier == bookIdIdentifier) {
                continue;
            }
            serializer.startTag("http://purl.org/dc/elements/1.1/", "identifier");
            serializer.attribute("http://www.idpf.org/2007/opf", "scheme", identifier.getScheme());
            serializer.text(identifier.getValue());
            serializer.endTag("http://purl.org/dc/elements/1.1/", "identifier");
        }
    }
}
