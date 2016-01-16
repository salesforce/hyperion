package com.krux.hyperion.common

trait NamedPipelineObject extends PipelineObject {

  type Self <: NamedPipelineObject

  def baseFields: BaseFields
  def updateBaseFields(fields: BaseFields): Self

  def id = baseFields.id

  /**
   * Name of the pipeline object, if not set, it will defaults to {{{Option(id)}}}
   */
  def name = baseFields.name.orElse(id.toOption)

  /**
   * Give the object a name prefix
   */
  def named(namePrefix: String) = updateBaseFields(
    baseFields.copy(name = baseFields.name.map(namePrefix + "_" + _ ).orElse(Option(namePrefix)))
  )

  /**
   * Postfix the name field
   */
  def groupedBy(group: String) = updateBaseFields(
    baseFields.copy(name = baseFields.name.map(_ + "_" + group).orElse(Option(group)))
  )

  /**
   * Id field will be prefixed with name
   *
   * @note Id naming is more restrictive, it is recommended to not changing the id unless you have
   * a good reason
   */
  def idNamed(namePrefix: String) = updateBaseFields(
    baseFields.copy(id = baseFields.id.named(namePrefix))
  )

  /**
   * Have a grouping postfix in the id field
   *
   * @note Id naming is more restrictive, it is recommended to not changing the id unleass you have
   * a good reason
   */
  def idGroupedBy(group: String) = updateBaseFields(
    baseFields.copy(id = baseFields.id.groupedBy(group))
  )

}
