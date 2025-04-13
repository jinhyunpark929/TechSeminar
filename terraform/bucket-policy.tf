# bucket-policy.tf

resource "aws_s3_bucket_policy" "ts_bucket_public_read" {
  bucket = aws_s3_bucket.ts_bucket.id

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Sid       = "PublicReadGetObject"
        Effect    = "Allow"
        Principal = "*"
        Action    = "s3:GetObject"
        Resource  = "${aws_s3_bucket.ts_bucket.arn}/*"
      }
    ]
  })
}
