@import hexlet.code.dto.urls.UrlPage
@import hexlet.code.utils.NamedRoutes

@param UrlPage page

@template.layout.page(
    page = page,
    content = @`
        <main>
            <div>
                <h1>Website: ${page.getUrl().getName()}</h1>
                <p>Created at: ${page.getUrl().getCreatedAtFormatted()}</p>
            </div>
            <div class="d-flex gap-2 justify-content-left py-5">
                <form action="${NamedRoutes.checkPath(page.getUrl().getId())}" method="post">
                    <button class="btn btn-primary d-inline-flex align-items-center" type="submit">
                        Check
                    </button>
                </form>
            </div>
            @if(page.getChecks().isEmpty())
                <p>No started checks yet.</p>
            @endif
            <table class="table table-striped">
                <thead>
                <tr>
                    <th scope="col">ID</th>
                    <th scope="col">Status code</th>
                    <th scope="col">H1</th>
                    <th scope="col">Title</th>
                    <th scope="col">Description</th>
                    <th scope="col">Check started</th>
                </tr>
                </thead>
                <tbody>
                @if(!page.getChecks().isEmpty())
                    @for(var check : page.getChecks())
                        <tr>
                            <th scope="row">${check.getId()}</th>
                            <td>${check.getStatusCode()}</td>
                            <td>${check.getH1()}</td>
                            <td>${check.getTitle()}</td>
                            <td>${check.getDescription()}</td>
                            <td>${check.getCreatedAtFormatted()}</td>
                        </tr>
                    @endfor
                @endif
                </tbody>
            </table>
        </main>
    `
)