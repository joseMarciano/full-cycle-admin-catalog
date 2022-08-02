package com.fullcyle.admin.catalog.infastructure.api;

import com.fullcyle.admin.catalog.domain.pagination.Pagination;
import com.fullcyle.admin.catalog.infastructure.genre.models.CreateGenreRequest;
import com.fullcyle.admin.catalog.infastructure.genre.models.GenreListResponse;
import com.fullcyle.admin.catalog.infastructure.genre.models.GenreResponse;
import com.fullcyle.admin.catalog.infastructure.genre.models.UpdateGenreRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("genres")
@Tag(name = "Genres")
public interface GenreAPI {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Create a new genre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created successfully"),
            @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    ResponseEntity<?> create(@RequestBody CreateGenreRequest input);



    @GetMapping
    @Operation(summary = "List all genres paginated")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listed successfully"),
            @ApiResponse(responseCode = "422", description = "A invalid parameter was received"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
        Pagination<GenreListResponse> list(
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int perPage,
            @RequestParam(required = false, defaultValue = "name") String sort,
            @RequestParam(name = "dir", required = false, defaultValue = "asc") String direction
    );

    @GetMapping(
            value = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Get a genre by it's identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Genre retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Genre was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    GenreResponse getById(@PathVariable String id);


    @PutMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update a genre by identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Genre update successfully"),
            @ApiResponse(responseCode = "404", description = "Genre was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    ResponseEntity<?> updateById(@PathVariable String id, @RequestBody UpdateGenreRequest body);

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Delete a genre by identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Genre deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Genre was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    void deleteById(@PathVariable String id);
}
