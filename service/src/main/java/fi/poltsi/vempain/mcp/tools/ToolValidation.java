package fi.poltsi.vempain.mcp.tools;

public final class ToolValidation {

    private ToolValidation() {
    }

    public static int page(int page) {
        if (page < 0) {
            throw new IllegalArgumentException("page must be non-negative");
        }
        return page;
    }

    public static int size(int size) {
        if (size < 1 || size > 500) {
            throw new IllegalArgumentException("size must be between 1 and 500");
        }
        return size;
    }
}
