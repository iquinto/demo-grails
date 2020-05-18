package demo

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class BandController {

    BandService bandService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond bandService.list(params), model:[bandCount: bandService.count()]
    }

    def show(Long id) {
        respond bandService.get(id)
    }

    def create() {
        respond new Band(params)
    }

    def save(Band band) {
        if (band == null) {
            notFound()
            return
        }

        try {
            bandService.save(band)
        } catch (ValidationException e) {
            respond band.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'band.label', default: 'Band'), band.id])
                redirect band
            }
            '*' { respond band, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond bandService.get(id)
    }

    def update(Band band) {
        if (band == null) {
            notFound()
            return
        }

        try {
            bandService.save(band)
        } catch (ValidationException e) {
            respond band.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'band.label', default: 'Band'), band.id])
                redirect band
            }
            '*'{ respond band, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        bandService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'band.label', default: 'Band'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'band.label', default: 'Band'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
