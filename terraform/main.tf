# main.tf

provider "aws" {
  region = "ap-northeast-2"
}

resource "aws_s3_bucket" "ts_bucket" {
  bucket = "tech-seminar-bucket-${random_id.suffix.hex}"
  force_destroy = true
}

resource "random_id" "suffix" {
  byte_length = 4
}
