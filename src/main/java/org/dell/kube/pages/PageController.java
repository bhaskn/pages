package org.dell.kube.pages;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import ch.qos.logback.classic.Logger;

import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/pages")
public class PageController {
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
        Page newPage=null;
        if(page!=null)
            newPage= pageRepository.create(page);
        else {
            logger.error("CREATE-ERROR:Creating the page = " + page);
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Page>(newPage, HttpStatus.CREATED);

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