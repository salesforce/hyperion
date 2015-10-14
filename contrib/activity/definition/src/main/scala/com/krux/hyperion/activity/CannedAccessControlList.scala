package com.krux.hyperion.activity

/**
 * mirror of com.amazonaws.services.s3.model.CannedAccessControlList to avoid unnecessary
 * dependency.
 */
object CannedAccessControlList extends Enumeration {

  val Private = Value("private")
  val PublicRead = Value("public-read")
  val PublicReadWrite = Value("public-read-write")
  val AuthenticatedRead = Value("authenticated-read")
  val BucketOwnerRead = Value("bucket-owner-read")
  val BucketOwnerFullControl = Value("bucket-owner-full-control")
  val LogDeliveryWrite = Value("log-delivery-write")

}
