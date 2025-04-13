# website.tf

resource "aws_s3_bucket_website_configuration" "ts_website_config" {
  bucket = aws_s3_bucket.ts_bucket.id

  index_document {
    suffix = "index.html"
  }

  error_document {
    key = "error.html"
  }
}
