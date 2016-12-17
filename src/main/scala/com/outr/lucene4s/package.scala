package com.outr

import com.outr.lucene4s.field.Field
import com.outr.lucene4s.field.value.{FieldAndValue, SpatialPoint}
import com.outr.lucene4s.field.value.support._
import com.outr.lucene4s.query._
import squants.space._

import scala.language.implicitConversions

package object lucene4s {
  implicit def stringSupport: ValueSupport[String] = StringValueSupport
  implicit def booleanSupport: ValueSupport[Boolean] = BooleanValueSupport
  implicit def intSupport: ValueSupport[Int] = IntValueSupport
  implicit def longSupport: ValueSupport[Long] = LongValueSupport
  implicit def doubleSupport: ValueSupport[Double] = DoubleValueSupport
  implicit def spatialPointSupport: ValueSupport[SpatialPoint] = SpatialPointValueSupport

  implicit def string2ParsableSearchTerm(value: String): SearchTerm = parse(value)

  implicit val booleanFieldValue2SearchTerm = (fv: FieldAndValue[Boolean]) => new ExactBooleanSearchTerm(fv.field, fv.value)
  implicit val intFieldValue2SearchTerm = (fv: FieldAndValue[Int]) => new ExactIntSearchTerm(fv.field, fv.value)
  implicit val longFieldValue2SearchTerm = (fv: FieldAndValue[Long]) => new ExactLongSearchTerm(fv.field, fv.value)
  implicit val doubleFieldValue2SearchTerm = (fv: FieldAndValue[Double]) => new ExactDoubleSearchTerm(fv.field, fv.value)
  implicit val stringFieldValue2SearchTerm = (fv: FieldAndValue[String]) => new TermSearchTerm(Some(fv.field), s""""${fv.value.toLowerCase}"""")

  def matchAll(): SearchTerm = MatchAllSearchTerm

  def parse(field: Field[String], value: String): ParsableSearchTerm = parse(field, value, allowLeadingWildcard = false)
  def parse(field: Field[String], value: String, allowLeadingWildcard: Boolean): ParsableSearchTerm = new ParsableSearchTerm(Some(field), value, allowLeadingWildcard)
  def parse(value: String): ParsableSearchTerm = parse(value, allowLeadingWildcard = false)
  def parse(value: String, allowLeadingWildcard: Boolean): ParsableSearchTerm = new ParsableSearchTerm(None, value, allowLeadingWildcard)

  def term(fv: FieldAndValue[String]): TermSearchTerm = new TermSearchTerm(Some(fv.field), fv.value.toString.toLowerCase)
  def term(value: String): TermSearchTerm = new TermSearchTerm(None, value)

  def exact[T](fv: FieldAndValue[T])(implicit fv2SearchTerm: FieldAndValue[T] => SearchTerm): SearchTerm = fv2SearchTerm(fv)
  def intRange(field: Field[Int], start: Int, end: Int): SearchTerm = new RangeIntSearchTerm(field, start, end)
  def longRange(field: Field[Long], start: Long, end: Long): SearchTerm = new RangeLongSearchTerm(field, start, end)
  def doubleRange(field: Field[Double], start: Double, end: Double): SearchTerm = new RangeDoubleSearchTerm(field, start, end)

  def regexp(fv: FieldAndValue[String]): RegexpSearchTerm = new RegexpSearchTerm(Some(fv.field), fv.value.toString)
  def regexp(value: String): RegexpSearchTerm = new RegexpSearchTerm(None, value)

  def wildcard(fv: FieldAndValue[String]): WildcardSearchTerm = new WildcardSearchTerm(Some(fv.field), fv.value.toString)
  def wildcard(value: String): WildcardSearchTerm = new WildcardSearchTerm(None, value)

  def fuzzy(value: String): FuzzySearchTerm = new FuzzySearchTerm(None, value)
  def fuzzy(fv: FieldAndValue[String]): FuzzySearchTerm = new FuzzySearchTerm(Some(fv.field), fv.value.toString)

  def spatialBox(field: Field[SpatialPoint], minLatitude: Double, maxLatitude: Double, minLongitude: Double, maxLongitude: Double): SpatialBoxTerm = new SpatialBoxTerm(field, minLatitude, maxLatitude, minLongitude, maxLongitude)

  def spatialDistance(field: Field[SpatialPoint], point: SpatialPoint, radius: Length): SpatialDistanceTerm = new SpatialDistanceTerm(field, point, radius)

  def spatialPolygon(field: Field[SpatialPoint], polygons: SpatialPolygon*): SpatialPolygonTerm = new SpatialPolygonTerm(field, polygons.toList)

  def grouped(disableCoord: Boolean,
              minimumNumberShouldMatch: Int,
              entries: (SearchTerm, Condition)*): GroupedSearchTerm = new GroupedSearchTerm(
    disableCoord = disableCoord,
    minimumNumberShouldMatch = minimumNumberShouldMatch,
    conditionalTerms = entries.toList
  )
  def grouped(entries: (SearchTerm, Condition)*): GroupedSearchTerm = grouped(false, 0, entries: _*)
}