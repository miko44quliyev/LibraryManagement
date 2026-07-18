package librarymanagement.controller;

import jakarta.validation.Valid;
import librarymanagement.dto.request.MemberRequest;
import librarymanagement.dto.response.MemberResponse;
import librarymanagement.service.MemberService;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Tag(name = "Members", description = "Member management endpoints")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    @Operation(summary = "Create a member")
    public ResponseEntity<MemberResponse> create(@Valid @RequestBody MemberRequest request) {
        MemberResponse response = memberService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "List members with pagination and sorting")
    public ResponseEntity<Page<MemberResponse>> getAll(@PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(memberService.getAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a member by id")
    public ResponseEntity<MemberResponse> getById(@PathVariable Long id) {
        MemberResponse member = memberService.getById(id);
        return ResponseEntity.ok(member);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a member")
    public ResponseEntity<MemberResponse> update(
            @PathVariable Long id,
            @Valid
            @RequestBody MemberRequest request) {

        MemberResponse updatedMember = memberService.update(id, request);
        return ResponseEntity.ok(updatedMember);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a member")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        memberService.delete(id);
        return ResponseEntity.noContent().build();
    }
}