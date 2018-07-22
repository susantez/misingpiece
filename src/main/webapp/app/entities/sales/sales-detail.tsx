import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './sales.reducer';
import { ISales } from 'app/shared/model/sales.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISalesDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export class SalesDetail extends React.Component<ISalesDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { salesEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="misingpieceApp.sales.detail.title">Sales</Translate> [<b>{salesEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="type">
                <Translate contentKey="misingpieceApp.sales.type">Type</Translate>
              </span>
            </dt>
            <dd>{salesEntity.type}</dd>
            <dt>
              <span id="unitPrice">
                <Translate contentKey="misingpieceApp.sales.unitPrice">Unit Price</Translate>
              </span>
            </dt>
            <dd>{salesEntity.unitPrice}</dd>
            <dt>
              <span id="itemCount">
                <Translate contentKey="misingpieceApp.sales.itemCount">Item Count</Translate>
              </span>
            </dt>
            <dd>{salesEntity.itemCount}</dd>
            <dt>
              <span id="totalPrice">
                <Translate contentKey="misingpieceApp.sales.totalPrice">Total Price</Translate>
              </span>
            </dt>
            <dd>{salesEntity.totalPrice}</dd>
            <dt>
              <span id="creationDate">
                <Translate contentKey="misingpieceApp.sales.creationDate">Creation Date</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={salesEntity.creationDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="updateDate">
                <Translate contentKey="misingpieceApp.sales.updateDate">Update Date</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={salesEntity.updateDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="status">
                <Translate contentKey="misingpieceApp.sales.status">Status</Translate>
              </span>
            </dt>
            <dd>{salesEntity.status}</dd>
            <dt>
              <Translate contentKey="misingpieceApp.sales.product">Product</Translate>
            </dt>
            <dd>{salesEntity.product ? salesEntity.product.id : ''}</dd>
            <dt>
              <Translate contentKey="misingpieceApp.sales.event">Event</Translate>
            </dt>
            <dd>
              {salesEntity.events
                ? salesEntity.events.map((val, i) => (
                    <span key={val.id}>
                      <a>{val.id}</a>
                      {i === salesEntity.events.length - 1 ? '' : ', '}
                    </span>
                  ))
                : null}
            </dd>
          </dl>
          <Button tag={Link} to="/entity/sales" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/sales/${salesEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ sales }: IRootState) => ({
  salesEntity: sales.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(SalesDetail);
