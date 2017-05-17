package service.solrclient;

public interface ISolrClient {
	
    void SetSolrHost();

    void SetSolrCore(String core);

    void InitSolrClient(String host, String core);
}
