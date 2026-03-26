package tracker.userservice.web.exception

import jakarta.servlet.http.HttpServletRequest
import labs.catmarket.repository.exception.JpaEntityNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import tracker.userservice.application.exception.DomainAlreadyExistsException
import tracker.userservice.application.exception.DomainNotFoundException
import tracker.userservice.application.exception.InvalidCredentialsException
import java.net.URI

@ControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {

    private val log = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler(DomainNotFoundException::class)
    fun handleDomainNotFoundException(ex: DomainNotFoundException, request: HttpServletRequest): ProblemDetail {
        val problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.message)
        problem.title = "Not Found"
        problem.type = URI.create("https://example.com/errors/not-found")
        problem.instance = URI.create(request.requestURI)
        log.warn(ex.message)
        return problem
    }

    @ExceptionHandler(JpaEntityNotFoundException::class)
    fun handleJpaEntityNotFoundException(ex: JpaEntityNotFoundException, request: HttpServletRequest): ProblemDetail {
        val problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.message)
        problem.title = "Not Found"
        problem.type = URI.create("https://example.com/errors/not-found")
        problem.instance = URI.create(request.requestURI)
        log.warn(ex.message)
        return problem
    }

    @ExceptionHandler(InvalidCredentialsException::class)
    fun handleInvalidCredentialsException(ex: InvalidCredentialsException, request: HttpServletRequest): ProblemDetail {
        val problem = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.message)
        problem.title = "Unauthorized"
        problem.type = URI.create("https://example.com/errors/not-found")
        problem.instance = URI.create(request.requestURI)
        log.warn(ex.message)
        return problem
    }

    @ExceptionHandler(DomainAlreadyExistsException::class)
    fun handleDomainAlreadyExistsException(
        ex: DomainAlreadyExistsException,
        request: HttpServletRequest
    ): ProblemDetail {
        val problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.message)
        problem.title = "Conflict"
        problem.type = URI.create("https://example.com/errors/conflict")
        problem.instance = URI.create(request.requestURI)
        log.warn(ex.message)
        return problem
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException, request: HttpServletRequest): ProblemDetail {
        val problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.message ?: "Bad Request")
        problem.title = "Bad Request"
        problem.type = URI.create("https://example.com/errors/bad-request")
        problem.instance = URI.create(request.requestURI)
        log.warn(ex.message)
        return problem
    }

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any> {

        val errors = ex.bindingResult.fieldErrors
            .associate { it.field to (it.defaultMessage ?: "Invalid value") }

        val problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed")
        problem.title = "Bad Request"
        problem.type = URI.create("https://example.com/errors/validation-failed")
        problem.instance = URI.create(request.getDescription(false).removePrefix("uri="))
        problem.setProperty("errors", errors)
        log.warn(ex.message)
        return ResponseEntity(problem, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception::class)
    fun handleAll(ex: Exception, request: HttpServletRequest): ProblemDetail {
        val problem =
            ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.message ?: "Internal Server Error")
        problem.title = "Internal Server Error"
        problem.type = URI.create("https://example.com/errors/internal-server-error")
        problem.instance = URI.create(request.requestURI)
        log.warn(ex.message)
        return problem
    }
}
