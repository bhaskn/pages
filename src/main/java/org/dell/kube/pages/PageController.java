package org.dell.kube.pages;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import ch.qos.logback.classic.Logger;

import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/pages")
public class PageController {

    @Autowired
    CategoryClient categoryClient;

    private IPageRepository pageRepository;
    Logger logger =(Logger)LoggerFactory.getLogger(this.getClass());

    public PageController(IPageRepository pageRepository)
    {
        this.pageRepository = pageRepository;
    }
    @PostMapping
    public ResponseEntity<Page> create(@RequestBody Page page) {

        logger.info("CREATE-INFO:Creating the page = " + page);
        logger.debug("CREATE-DEBUG:Creating the page = " + page);
        Category category = null;
        try {
            category = categoryClient.findCategory(page.getCategoryId());
        }
        catch(FeignException ex){
            if(ex.getMessage().contains("404")) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            else{
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        if(category ==null || category.getId()==null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else
        {
            Page newPage = pageRepository.create(page);
            logger.info("CREATE-INFO:Created a new page with id = " + newPage.id);
            logger.debug("CREATE-DEBUG:Created a new  page with id = " + newPage.id);
            return new ResponseEntity<Page>(newPage, HttpStatus.CREATED);
        }

    }
    @GetMapping("{id}")
    public ResponseEntity<Page> read(@PathVariable long id) {

        logger.info("READ-INFO:Fetching page with id = " + id);
        logger.debug("READ-DEBUG:Fetching page with id = " + id);

        Page page = pageRepository.read(id);
        if(page!=null)
            return new ResponseEntity<Page>(page, HttpStatus.OK);
        else {
            logger.error("READ-ERROR:Could not find page with id = " + id);
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping
    public ResponseEntity<List<Page>> list() {
        logger.info("READ-INFO:Fetching list " );
        logger.debug("READ-DEBUG:Fetching list" );

        List<Page> pages= pageRepository.list();
        logger.info("READ-INFO:Returning list of size =" +pages.size());
        return new ResponseEntity<List<Page>>(pages, HttpStatus.OK);
    }
    @PutMapping("{id}")
    public ResponseEntity<Page> update(@RequestBody Page page, @PathVariable long id) {
        logger.info("UPDATE-INFO:Fetching page with id = " + id);
        logger.debug("UPDATE-DEBUG:Fetching page with id = " + id);
        Page updatedPage= pageRepository.update(page,id);
        if(updatedPage!=null)
            return new ResponseEntity<Page>(updatedPage, HttpStatus.OK);
        else{
            logger.error("UPDATE-ERROR:Could not delete page with id = " + id);
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable long id) {
        logger.info("DELETE-INFO:Fetching page with id = " + id);
        logger.debug("DELETE-DEBUG:Fetching page with id = " + id);
        pageRepository.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}