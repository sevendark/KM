package com.asiainfo.km.repository.solr;

import com.asiainfo.km.domain.solr.DocSolrInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.SolrResultPage;
import org.springframework.data.solr.repository.Highlight;
import org.springframework.data.solr.repository.SolrCrudRepository;

public interface DocSolrRepo extends SolrCrudRepository<DocSolrInfo,Long> {
    DocSolrInfo findByDocId(Long docId);
    @Highlight(prefix = "</xmp><b style=\"background:yellow\">", postfix = "</b><xmp>" )
    SolrResultPage<DocSolrInfo> findByDocNameOrDocIntro(String docName, String docIntro, Pageable pageable);
}
